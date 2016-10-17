package p2MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import dataManagementClasses.TableSchema;
import tableCollectionClasses.Record;
import tableCollectionClasses.Table;

public class TableAnalyzer {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Illegal Argument. Try again");
			System.exit(0);
		}

		Scanner sc = new Scanner(System.in);
		Boolean moreAnalysis;
		ArrayList<Integer> selectedAttributes;
		Map<String, Integer> rF;

		RandomAccessFile raf = null;
		TableSchema ts = null;
		Table t = null;

		try {
			File f = new File("src/inputData/" + args[0]);
			if (!f.exists()) {
				System.out.println("File doesn't exist");
				System.exit(0);

			} else {

				raf = new RandomAccessFile(f, "rw");
				ts = TableSchema.getInstance(raf);

				if (!ts.isValid(raf)) {
					System.out.println("FILE NOT VALID!");
					System.exit(0);
				}

				t = new Table(ts);

				if (!(raf.length() == raf.getFilePointer())) {
					t.readTableDataFromFile(raf);
				}

			}

		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (IllegalStateException e) {
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
			System.exit(0);
		}

		t.displayTable();
		System.out.println();
		System.out.println("COLUMN NUMBERS:");
		t.displayAttributeTable();
		System.out.println();

		moreAnalysis = wantMoreAnalysis(sc);
		do {

			if (!moreAnalysis) {
				System.out.println("TERMINATED.");
				System.exit(0);
			} else {
				rF = new HashMap<String, Integer>();
				selectedAttributes = selectAttributes(t.getNumberOfAttrs(), sc);
				if (selectedAttributes.size() == 0) {
					System.out.println("NO ATTRIBUTES SELECTED.");
					System.exit(0);
				} else {
					for (int i = 0; i < t.getNumberOfRecords(); i++) {
						Record r = t.getRecord(i);

						String rA = "";
						for (int attrInd : selectedAttributes) {
							rA = rA.concat(r.readData(attrInd - 1).toString() + ",");
						}
						rA = rA.substring(0, rA.length()-1);
						
						

						if (rF.containsKey(rA)) {
							rF.put(rA, rF.get(rA) + 1);
						} else {
							rF.put(rA, 1);
						}
					}

					String analysisSchema = "||\t";
					for (int a : selectedAttributes) {
						analysisSchema = analysisSchema.concat(t.getAttribute(a - 1).getName() + ",");
					}
					analysisSchema = analysisSchema.substring(0, analysisSchema.length()-1);
					analysisSchema = analysisSchema.concat("\t" + "Frequency\t" + "Percentage\t" + "||");
					System.out.println(analysisSchema);

					for (int i = 0; i < analysisSchema.length() * 2 - 12; i++) {
						System.out.print("=");
					}
					System.out.println();
					
					Object[] keyArray = rF.keySet().toArray();
					Object[] freqArray = rF.values().toArray();
					
					
					DecimalFormat dF = new DecimalFormat("#.##");
					for (int i = 0; i < rF.size(); i++) {
						Double p = ((Double.parseDouble(freqArray[i].toString()) / t.getNumberOfRecords()) * 100);
						
						System.out.println("||\t" + keyArray[i].toString() + "\t\t" + freqArray[i] + "\t" + dF.format(p) + "\t||");
					}

				}
			}

			moreAnalysis = wantMoreAnalysis(sc);
		} while (moreAnalysis);

	}

	/**
	 * Ask if the user wants to analyze attribute from the table
	 * @param sc Scanner to read the user input
	 * @return TRUE if the user wants analyze attributes, FALSE otherwise
	 */
	public static Boolean wantMoreAnalysis(Scanner sc) {
		Boolean b = false;
		do {
			System.out.println("Do you want to analyze attributes: YES-(TRUE) NO-(FALSE)");
			try {
				b = sc.nextBoolean();
				if (!b)
					return false;
			} catch (InputMismatchException e) {
				System.out.println("WRONG INPUT. TRY AGAIN.");
			}
			sc.nextLine();
		} while (!b);

		return b;
	}

	/**
	 * It manages the selection of attributes
	 * @param totalAttr The total of attributes in the table
	 * @param sc Scanner to read the user input
	 * @return An ArrayList of integers that have the selections
	 */
	public static ArrayList<Integer> selectAttributes(int totalAttr, Scanner sc) {
		ArrayList<Integer> sA = new ArrayList<Integer>();
		Boolean stopProcess = false;
		do {
			System.out.print("Next Attribute: ");
			try {
				int input = sc.nextInt();
				sc.nextLine();
				if (input > 0) {
					if (!(input > totalAttr)) {

						if ((sA.indexOf(input) < 0) || sA.isEmpty())
							sA.add(input);
						else
							System.out.println("ATTRIBUTE ALREADY SELECTED.");
					} else {
						System.out.println("INVALID ENTRY.");
						continue;
					}
				} else if (input <= 0) {
					System.out.println("SELECTION TERMINATED.");
					stopProcess = true;
				}

			} catch (InputMismatchException e) {
				System.out.println("WRONG INPUT. TRY AGAIN.");
				sc.nextLine();
			}
		} while (!stopProcess);

		return sA;
	}

}
