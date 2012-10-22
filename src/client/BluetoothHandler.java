package client;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothHandler {
	
	private InputStream in;
    private OutputStream out;
    private String portName;
    private static int BAUDRATE = 115200;
	
	public BluetoothHandler(String portName) {
		this.portName = portName;
	}
	
	public void connect() throws Exception {
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new IOException("Port is in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(BAUDRATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
            }
        } 
	}
	
	public void disconnect() throws Exception {
		out.close();
		in.close();
	}
	
	public void sendInts(int[] values) throws Exception{
		for(int i = 0 ; i < values.length ; i++) {
			byte firstByte = (byte)values[i];
			byte secondByte = (byte)(values[i]>>>8);
			this.out.write(firstByte);
			this.out.write(secondByte);
		}		
	}
	
	public int[] readInts(int numberOfInts) throws Exception{
		byte[] bytes = readBytes(numberOfInts*2);
		int[] ints = new int[numberOfInts];

		for(int i = 0, j = 0 ; i < bytes.length ; i+=2, j++)
			ints[j] = (int)bytes[i+1]<<8 | (int)bytes[i] & 0x000000FF;
		
		return ints;
	}
	
	private byte[] readBytes(int numberOfBytes) throws Exception{
    	byte[] realBytes = new byte[numberOfBytes];
		int j = 0;
    	
    	do{
        	byte[] bytes = new byte[100];
			int len = in.read(bytes);
			for(int i = 0 ; i < len && j < numberOfBytes ; i++) 
				realBytes[j++] = bytes[i];
			
    	}while(j < numberOfBytes);
	
    	return realBytes;
    }
}