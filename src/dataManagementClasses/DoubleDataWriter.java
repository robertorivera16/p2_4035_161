package dataManagementClasses;

import generalUtilities.DataUtils;
import interfaces.DataWriter;

public class DoubleDataWriter implements DataWriter{

	private static final int DOUBLESIZE = Double.BYTES; 
	public static final DoubleDataWriter INSTANCE = new DoubleDataWriter(); 
	
	private DoubleDataWriter() {}; 
	
	public void writeDataToArrayOfBytes(byte[] b, int index, Object rv) {
		Double v = (Double) rv; 
		Long value = Double.doubleToLongBits(v); 
		Long lSB; 
		for (int i=0; i < DOUBLESIZE; i++) { 
			lSB = 0x000000ff & value;
			value = value >> 8; 
		    b[index + DOUBLESIZE - i - 1] = (byte) (lSB & 0x000000ff); 
		}
 
	}

	@Override
	public String toString(Object value) {
		return String.format(DataUtils.FLOATFORMAT, (Double) value); 
		//TODO
	}
	
}