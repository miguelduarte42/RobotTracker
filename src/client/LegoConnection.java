package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class LegoConnection extends Thread{
	private boolean BT = true;

//	private DataOutputStream output;
	private DataInputStream input;
	private RemoteExperiment exp;
	private String NXT_ADDR = "00:16:53:0e:4d:69";
	
	public LegoConnection(RemoteExperiment exp) {
		this.exp = exp;
	}

	public void connect() {
//		NXTInfo nxtCtrl = new NXTInfo();
//		NXTConnector conn = new NXTConnector();

		if (BT) {
			try {
				NXTComm com = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", NXT_ADDR);
				//NXTComm com = new NXTCommBluecove();
				com.open(nxtInfo);
				System.out.println("Connection established!");
				
//				output = new DataOutputStream(com.getOutputStream());
				input = new DataInputStream(com.getInputStream());
				
				DataReceiver receiver = new DataReceiver(input);
				receiver.start();
			} catch (Exception e) {
				System.err.println("Could not establish connection.");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		connect();
		while(true);
//		try {
//			while(true) {
//				if(sendCommand) {
//					output.write(1);
//					output.flush();
//					sendCommand = false;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

//	public void stopSending() {
//		try {
//			output.close();
//			input.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private class DataReceiver extends Thread {

		private InputStream inputStream;

		public DataReceiver(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public void run() {
			try {
				while (true) {
					System.out.println("waiting to read");
					int received = inputStream.read();
					System.out.println("read");
					if(received == 1)
						exp.warnDoors();
				}
			} catch (Exception e) {
				System.out.println("InputStream died");
			}
			connect();
		}
	}
	public static void main(String[] args) {
		LegoConnection lc = new LegoConnection(null);
		lc.start();
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("damn");
//		lc.sendCommand = true;
		
		while(true);
	}
}
