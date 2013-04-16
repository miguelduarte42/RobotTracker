package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import tracking.Robot;

/**
 * Receives the location information from the
 * Location Server. The information comes in the
 * form of a Robot object.
 * 
 * @author miguelduarte
 *
 */
public class LocationClient {
	
	private Socket socket;
	private static String IP = "10.40.50.132";
	private static int PORT = 1338;
	private InputHandler input;
	private OutputHandler output;
	
	public double x;
	public double y;
	public double orientation;
	
	public LocationClient() {
		try {
			socket = new Socket(IP, PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		input = new InputHandler();
		output = new OutputHandler();
		
		input.start();
	}
	
	public void sendObjectMessage(Robot r) {
		output.sendObjectMessage(r);
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
	
	public static void main(String[] args) {
		LocationClient lc = new LocationClient();
		while(true) {
			Scanner s = new Scanner(System.in);
			Robot r = new Robot();
			r.x = s.nextInt();
			r.y = s.nextInt();
			r.orientation = 1;
			lc.sendObjectMessage(r);
		}
	}
}
