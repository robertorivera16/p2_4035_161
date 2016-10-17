package tableCollectionClasses;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import dataManagementClasses.AttributeInSchema;
import dataManagementClasses.TableSchema;

public class Table {
	private TableSchema ts;
	private ArrayList<Record> rList;
	private int recordLength;

	/**
	 * Construct a table with the information given by the parameter that it receives.
	 * @param ts TableSchema
	 */
	public Table(TableSchema ts) {
		this.ts = ts;
		recordLength = ts.getDataRecordLength();
		rList = new ArrayList<>();
	}

	/**
	 * Returns the Attribute in Schema in the given index
	 * @param index index of the attribute in the schema
	 * @return Returns the Attribute in Schema in the given index
	 */
	public AttributeInSchema getAttribute(int index) {
		return ts.getAttr(index);
	}

	/**
	 * @return The number of attributes in the tableSchema
	 */
	public int getNumberOfAttrs() {
		return ts.getNumberOfAttrs();
	}

	/**
	 * @return the number of rows or Records in the table
	 */
	public int getNumberOfRecords() {
		return rList.size();
	}

	/**
	 * @return a new instance of Record
	 */
	public Record getNewRecordInstance() {
		return new Record(ts);
	}

	public void addRecord(Record r) {
		rList.add(r);
	}

	public Record getRecord(int index) {
		return rList.get(index);
	}

	public int getRecordLength() {
		return recordLength;
	}

	public void displaySchema() {
		System.out.println(ts);
	}

	public void displayTable() {
		this.displaySchema();
		for (Record r : rList)
			System.out.println(r);
	}

	/**
	 * Displays a table consisting of name of attribute and its type; 
	 * also, with a number for the selection of them.  
	 */
	public void displayAttributeTable() {
		String s = "| ";
		for (int i = 0; i < getNumberOfAttrs(); i++) {
			s += getAttribute(i) + ": " + (i + 1) + " | ";
		}
		System.out.println(s);

	}

	public void readTableDataFromFile(RandomAccessFile file) throws IOException {

		long numberOfDataRecords = (file.length() - file.getFilePointer()) / ts.getDataRecordLength();

		for (int dr = 1; dr <= numberOfDataRecords; dr++) {
			Record record = getNewRecordInstance();
			record.readFromFile(file);
			addRecord(record);
		}

	}

	// public static Table readTableDataFromFile(RandomAccessFile file) throws
	// IOException {
	//
	// TableSchema ts = TableSchema.getInstance(file);
	// Table table = new Table(ts);
	// long numberOfDataRecords =
	// (file.length() - file.getFilePointer())/ts.getDataRecordLength();
	//
	// for (int dr = 1; dr <= numberOfDataRecords; dr++) {
	// Record record = table.getNewRecordInstance();
	// record.readFromFile(file);
	// table.addRecord(record);
	// }
	//
	// return table;
	// }

}
