package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tracking.Robot;

public class TrackerClient {
	
	private Socket socket;
	private static String IP = "10.40.50.134";
	private static int PORT = 1338;
	private InputHandler input;
	private OutputHandler output;
	
	public double x;
	public double y;
	public double orientation;
	
	public TrackerClient() {
		try {
			socket = new Socket(IP, PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		input = new InputHandler();
		output = new OutputHandler();
		
		input.start();
	}
	
	public void sendObjectMessage(Robot r) {
		output.sendObjectMessage(r);
	}

	public static void main(String[] args) {
		new TrackerClient();
	}
	
	class InputHandler extends Thread {
		@Override
		public void run() {
			try {
				
				ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
				
				while(true) {
					Robot r = (Robot)stream.readObject();
					orientation = r.orientation;
					x = r.x;
					y = r.y;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class OutputHandler {
		
		private ObjectOutputStream ostream;
		
		public OutputHandler() {
			try {
				ostream = new ObjectOutputStream(socket.getOutputStream());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public void sendObjectMessage(Robot r) {
			try {
				ostream.writeObject(r);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
