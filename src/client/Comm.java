package client;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Scanner;

public class Comm {
	
	static byte LED0_SELECTOR = 2,
				LED1_SELECTOR = 8,
				LED2_SELECTOR = 32,
				LED3_SELECTOR = -128,
				LED0_ON = 1,
				LED1_ON = 4,
				LED2_ON = 16,
				LED3_ON = 64,
				START = 110,
		        LED = 111,
		        WHEEL_LEFT = 112,
		        WHEEL_RIGHT = 113,
		        SENSORS = 114;
	
	public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() + ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }
	
	/** */
    public static class TestBytes implements Runnable 
    {
        InputStream in;
        OutputStream out;
        Scanner s;
        
        public TestBytes ( InputStream in, OutputStream out )
        {
            this.in = in;
            this.out = out;
            s = new Scanner(in);
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
        
        public void run()
        {
        	Scanner s = new Scanner(System.in);
        	
        	try {       
              
                int[] st = new int[]{START};
                sendInts(st, out);
//                
//                int a = 1;
//                
//                while(1==a) {
//                	byte[] bytes;
//                	bytes = readBytes(in, 1+6*2);
//                	
//                	if(bytes[0] == SENSORS) {
//
//                		for(int i = 1 ; i < bytes.length ; i+=2) {
//                			int value = (int)bytes[i+1]<<8 | (int)bytes[i] & 0x000000FF;
//                			double v = value;
//                			v/=1000.0;
//                			System.out.println(v);
//                		}
//            		}
//                	System.out.println();
//                }
                
                while (true)
                {
                	int inputs[] = readInts(1+6);
            		System.out.println("W or L");
            		String str = s.next();
            		
            		if(str.equals("W")) {
            			System.out.println("L or R");
//            			if(s.next().equals("L"))
//            				this.out.write(WHEEL_LEFT);
//            			else
//            				this.out.write(WHEEL_RIGHT);
            			
            			int[] ooo = new int[4];
            			
            			System.out.println("Speed");
            			int speed = s.nextInt() + 1000;
            			
            			ooo[0] = WHEEL_LEFT;
            			ooo[1] = speed;
            			ooo[2] = WHEEL_RIGHT;
            			ooo[3] = speed;
            			
            			sendInts(ooo, out);
            			
//            			System.out.println(speed+" speed");
//            			
//            			byte firstByte = (byte)speed;
//            			
//            			System.out.println((int)firstByte);
//            			System.out.println((int)(char)(speed>>>8));
//            			
//            			for(int i = 0 ; i < 8 ; i++) {
//            				System.out.print(isBitSet((byte)(speed>>>8),7-i) ? 1 : 0);
//            			}
//            			for(int i = 0 ; i < 8 ; i++) {
//            				System.out.print(isBitSet(firstByte,7-i) ? 1 : 0);
//            			}
            			
//            			this.out.write(firstByte);
//            			this.out.write(speed>>>8);
            			
            		}else if(str.equals("L")) {
            			this.out.write(LED);
            			
            			System.out.println("Number");
            			int number = s.nextInt();
            			System.out.println("State");
            			boolean state = s.nextInt()==1;
            			this.out.write(getLedBytes(number, state));
            		}
            	}          
            }catch ( Exception e ){e.printStackTrace();} 
                      
        }
    }
    
    static public void sendInts(int[] values, OutputStream out) throws Exception{
		for(int i = 0 ; i < values.length ; i++) {
			byte firstByte = (byte)values[i];
			byte secondByte = (byte)(values[i]>>>8);
			out.write(firstByte);
			out.write(secondByte);
		}		
	}
    
    public static boolean isBitSet(byte b, int bit)
    {
        return (b & (1 << bit)) != 0;
    }
    
    static byte[] readBytes(InputStream in, int numberOfBytes) throws Exception{
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
    
    static byte getLedBytes(int number, boolean state) {
    	switch(number) {
	    	case 0: return state ? (byte)(LED0_SELECTOR | LED0_ON) : (byte)LED0_SELECTOR;
	    	case 1: return state ? (byte)(LED1_SELECTOR | LED1_ON) : (byte)LED1_SELECTOR;
	    	case 2: return state ? (byte)(LED2_SELECTOR | LED2_ON) : (byte)LED2_SELECTOR;
	    	case 3: return state ? (byte)(LED3_SELECTOR | LED3_ON) : (byte)LED3_SELECTOR;
    	}
    	return 0;
    }
	
	void connect ( String portName ) throws Exception
    {

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new TestBytes(in,out))).start();
                
//                (new Thread(new SerialReader(in))).start();
//                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        Scanner s;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
            s = new Scanner(in);
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
//            try
//            {
            	
            	while(s.hasNext())
            		System.out.println(s.nextFloat());
//                while ( ( len = this.in.read(buffer)) > -1 )
//                {
//                	
////                	System.out.println();
////                	
////                	for(int i = 0 ; i < 1024 ; i++)
////                		System.out.print(buffer[i]+",");
////                	System.out.println("\n("+len+")");
//                    //System.out.print(new String(buffer,0,len));
//                }
//            }
//            catch ( IOException e )
//            {
//                e.printStackTrace();
//            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void writeItem(OutputStream out, String s) throws IOException
    {
        // Get the array of bytes for the string item:
        byte[] bs = s.getBytes(); // as bytes
        // Encapsulate by sending first the total length on 4 bytes :
        //   - bits 7..0 of length
        out.write(bs.length);      // modulo 256 done by write method
        //   - bits 15..8 of length
        out.write(bs.length>>>8);  // modulo 256 done by write method
        //   - bits 23..16 of length
        out.write(bs.length>>>16); // modulo 256 done by write method
        //   - bits 31..24 of length
        out.write(bs.length>>>24); // modulo 256 done by write method
        // Write the array content now:
        out.write(bs); // Send the bytes
        out.flush();
    }

    public static String readItem(InputStream in) throws IOException
    {
        // first, read the total length on 4 bytes
        //  - if first byte is missing, end of stream reached
        int len = in.read(); // 1 byte
        if (len<0) throw new IOException("end of stream");
        //  - the other 3 bytes of length are mandatory
        for(int i=1;i<4;i++) // need 3 more bytes:
        {
            int n = in.read();
            if (n<0) throw new IOException("partial data");
            len |= n << (i<<3); // shift by 8,16,24
        }
        // Create the array to receive len bytes:
        byte[] bs = new byte[len];
        // Read the len bytes into the created array
        int ofs = 0;
        while (len>0) // while there is some byte to read
        {
            int n = in.read(bs, ofs, len); // number of bytes actually read
            if (n<0) throw new IOException("partial data");
            ofs += n; // update offset
            len -= n; // update remaining number of bytes to read
        }
        // Transform bytes into String item:
        return new String(bs);
    }
    
    public static void main ( String[] args )
    {
        try
        {
            (new Comm()).connect("/dev/tty.e-puck_2426-COM1");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
