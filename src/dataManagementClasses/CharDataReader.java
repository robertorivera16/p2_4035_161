package dataManagementClasses;

import java.util.Scanner;

import generalUtilities.DataUtils;
import interfaces.DataReader;

public class CharDataReader implements DataReader {

	private static final int CHARSIZE = Character.BYTES; 
	public static final CharDataReader INSTANCE = new CharDataReader(); 
	
	private CharDataReader() {}; 
	
	public Character readDataFromArrayOfBytes(byte[] b, int index) {
		int value = 0; 
		int lSB; 
		for (int i=0; i < CHARSIZE; i++) { 
			value = value << 8; 
			lSB = 0x000000ff & b[index + i];
			value = value | lSB; 
		}
		return (char) value; 
	}
	
	@Override
	public Object readDataFromInputScanner(Scanner input) {
		String s = input.nextLine();
		
		try {
			Character c = new Character(s.charAt(0));
			return new Character(c);  
		} catch (Exception e) { 
			return null; 
		}
	}

}

