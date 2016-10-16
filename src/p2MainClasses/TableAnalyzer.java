package p2MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

		RandomAccessFile raf = null;
		TableSchema ts = null;
		Table t = null;

		try {
			File f = new File("src/inputData/" + args[0] + ".txt");
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

		if (!moreAnalysis) {
			System.out.println("TERMINATED.");
			System.exit(0);
		} else {

			selectedAttributes = selectAttributes(t.getNumberOfAttrs(), sc);
			if(selectedAttributes.size() == 0){
				System.out.println("NO ATTRIBUTES SELECTED.");
				System.exit(0);
			}else{
				for(int i : selectedAttributes){
					
				}
				
			}

		}

	}

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
						if (!(sA.indexOf(input) < 0))
							sA.add(input);
						else
							System.out.println("ATTRIBUTE ALREADY SELECTED.");
					} else {
						System.out.println("INVALID ENTRY.");
						continue;
					}
				} else if (input < 0) {
					System.out.println("SELECTION TERMINATED.");
					stopProcess = true;
				}

			} catch (InputMismatchException e) {
				System.out.println("WRONG INPUT. TRY AGAIN.");
			}
		} while (!stopProcess);

		return sA;
	}

}
