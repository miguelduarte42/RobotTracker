package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class LegoConnection extends Thread{
	private boolean BT = true;
	public boolean sendCommand = false;

	DataOutputStream output;
	DataInputStream input;
	boolean connected = false;

	public void send(int n) {
		if (connected)
			try {
				output.writeInt(n);
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void connect() {
//		NXTInfo nxtCtrl = new NXTInfo();
//		NXTConnector conn = new NXTConnector();

		if (BT) {
			try {
				NXTComm com = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00:16:53:0e:3a:b4");
				//NXTComm com = new NXTCommBluecove();
				com.open(nxtInfo);
				System.out.println("Connection established!");
				output = new DataOutputStream(com.getOutputStream());
				input = new DataInputStream(com.getInputStream());
				connected = true;
			} catch (Exception e) {
				System.err.println("Could not establish connection.");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		connect();
		try {
			while(true) {
				if(sendCommand) {
					output.write(1);
					output.flush();
					sendCommand = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopSending() {
		try {
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
