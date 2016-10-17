package p2MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;

import dataManagementClasses.AttributeInSchema;
import dataManagementClasses.TableSchema;
import generalUtilities.DataUtils;
import tableCollectionClasses.Record;
import tableCollectionClasses.Table;

public class DataFilePopulator {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Illegal Argument. Try again");
			System.exit(0);
		}

		Scanner sc = new Scanner(System.in);
		int numAttr = 0;
		String name, atttributeType;

		RandomAccessFile raf = null;
		TableSchema ts = null;
		Table t = null;

		try {
			File f = new File("src/inputData/" + args[0]);
			if (!f.exists()) {
				do {
					System.out.println("Enter number of attributes that it will contain");
					try {
						numAttr = sc.nextInt();
					} catch (Exception e) {
						sc.nextLine(); // Clean the buffer
					}

				} while (numAttr <= 0);

				sc.nextLine();// Clean the buffer

				ts = TableSchema.getInstance(numAttr);
				int offsetAtt = 0;

				// Create tableSchema
				for (int i = 0; i < numAttr; i++) {

					do {
						System.out.println("Enter name of attribute");
						name = sc.nextLine().toLowerCase();
					} while (!DataUtils.isValidName(name));

					do {
						System.out.println("Enter type of " + name + " attribute:");
						atttributeType = sc.nextLine().toLowerCase();
					} while (!DataUtils.isValidDataType(atttributeType));

					ts.addAttribute(new AttributeInSchema(name, DataUtils.getTypeID(atttributeType), offsetAtt));
					offsetAtt += DataUtils.getTypeSize(atttributeType);
				}

				f.createNewFile();
				raf = new RandomAccessFile(f, "rw");

				raf.seek(0);
				ts.saveSchema(raf);
				t = new Table(ts);
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
		
	
		
		int moreRec = 0;
		
		
		//Random Access File Created and the program starts to ask for new records
		do {
			System.out.println("Do you want to add records? ENTER NUMBER ONLY: YES-(1) NO-(2)");
			try {
				moreRec = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("INVALID INPUT!");
				sc.nextLine();
			}
		} while (moreRec != 1 && moreRec != 2);


		if (moreRec == 1) {
			do {
				Record r = t.getNewRecordInstance();
				r.readDataRecordFromUser(sc); 
				t.addRecord(r);
				do {
					moreRec = 0;
					System.out.println("Add more record? ENTER NUMBER ONLY: YES-(1) NO-(2)");
					try {
						moreRec = sc.nextInt();
						sc.nextLine();
					} catch (InputMismatchException e) {
						sc.nextLine();
						System.out.println("INVALID INPUT!");
					}
				} while (moreRec != 1 && moreRec != 2);
				
				

			} while (moreRec == 1);
		}

		try {
			raf.seek(ts.posFirstRec(raf));
		} catch (IOException e1) {

		}

		for (int i = 0; i < t.getNumberOfRecords(); i++) {
			try {
				t.getRecord(i).writeToFile(raf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		t.displayTable();

		System.out.println();
		System.out.println("TERMINATED");
		sc.close();

	}

}