package client;

public class RemoteDataReader {
	
	public static void main(String[] args) {
		BluetoothHandler bluetooth = new BluetoothHandler("/dev/tty.e-puck_2419-COM1");
		try {
			bluetooth.connect();
			System.out.println("OK");
			
			while(true) {
				int[] ints = bluetooth.readInts(4+1);
				if(ints[0] == 114) {
					for(int i = 1 ; i < 5 ; i++) {
						System.out.print(((double)ints[i])/1000.0+" ");
					}
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}