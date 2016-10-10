package dataManagementClasses;

import generalUtilities.DataUtils;
import interfaces.DataWriter;

public class ByteDataWriter implements DataWriter {

	private static final int BYTESIZE = Byte.BYTES; 
	public static final ByteDataWriter INSTANCE = new ByteDataWriter(); 
	
	private ByteDataWriter() {}; 
	
	public void writeDataToArrayOfBytes(byte[] b, int index, Object rvalue) {
		
		int value = (Byte) rvalue;
		int lSB; 
		for (int i=0; i < BYTESIZE; i++) { 
			lSB = 0x000000ff & value;
			value = value >> 8; 
		    b[index + BYTESIZE - i - 1] = (byte) (lSB & 0x000000ff); 
		}
	}

	@Override
	public String toString(Object value) {
		return String.format(DataUtils.INTEGERFORMAT, (Byte) value);
		//TODO
	}
	
}

