package client.epuck;

import client.BluetoothHandler;
import client.RemoteExperiment;
import tracking.Robot;

/**
 * This class remotely controls an e-puck robot.
 * It gets the sensor data from the robot every
 * 100ms, executes the control cycle of a Neural
 * Network, and sends the motor commands to the robot.
 * 
 * @author miguelduarte
 */
public class Controller extends Thread {
	
	private CTRNNMultilayer network;
	private BluetoothHandler bluetooth;
//	private  int MAX_SPEED = 800;
	public static int INPUTS = 4+8+4;
	public static int OUTPUTS = 2;
	private static double SENSORS = 114, WHEEL_LEFT = 112, WHEEL_RIGHT = 113, START = 110;
	private double mult = 0.8;//0.7;
	public boolean sensor = false;
	private String portName = "/dev/tty.e-puck_2426-COM1";
	private ControllerFrame frame;
	private double[] virtualInputs = new double[INPUTS];
	private Robot robot;
	private boolean moveRobot = true;
	public int timestep = 0;
	private RemoteExperiment experiment;
	
	public Controller(CTRNNMultilayer network) {
		this.network = network;
		bluetooth = new BluetoothHandler(portName);
	}
	
	public Controller(CTRNNMultilayer network,int inputs, int outputs) {
		INPUTS = inputs;
		OUTPUTS = outputs;
		virtualInputs = new double[INPUTS];
		this.network = network;
		bluetooth = new BluetoothHandler(portName);
	}
	
	public Controller(CTRNNMultilayer network, ControllerFrame frame) {
		this(network);
		this.frame = frame;
	}
	
	public Controller(CTRNNMultilayer network, String port) {
		this.portName = port;
		this.network = network;
		bluetooth = new BluetoothHandler(portName);
	}
	
	@Override
	public void run() {

		try {
			
			timestep = 0;
			
			bluetooth.connect();
			bluetooth.sendInts(new int[]{(int)START,(int)START});
			
			double speed = 680;
			
			while(true) {
				
				timestep++;
				
//				System.out.println(timestep);
				
				int inputs[] = bluetooth.readInts(1+4);
				
				double values[] = new double[INPUTS];
				
				for(int i = 0 ; i < virtualInputs.length ; i++)
					values[i] = virtualInputs[i];
				
				if(inputs[0] == SENSORS) {
					
					for(int i = 1 ; i < inputs.length ; i++) {
						values[i-1] = ((double)inputs[i])/1000.0;
						if(i == 0+1 || i == 3+1)
							values[i]*=mult;
					}
					
					//values[values.length-1] = sensor ? 1 : 0;
					
					double[] outputs = network.propagateInputs(values);
					
					int actuatorValues[] = new int[outputs.length*2];
					
					actuatorValues[0] = (int)WHEEL_LEFT;
					actuatorValues[1] = (int)((outputs[0]*2.0 - 1.0)*speed)+1000;
					actuatorValues[2] = (int)WHEEL_RIGHT;
					actuatorValues[3] = (int)((outputs[1]*2.0 - 1.0)*speed)+1000;
					
					if(outputs.length > 2 && outputs[2] > 0.5) {
						experiment.removeObject(robot.getPosition(),robot.preyPickRange);
						actuatorValues[1] = 1000;
						actuatorValues[3] = 1000;
					}
					
					if(!moveRobot) {
						actuatorValues[1] = 1000;
						actuatorValues[3] = 1000;
					}
					
					bluetooth.sendInts(actuatorValues);
					
					if(frame != null) {
						
						try {
							HierarchicalController c = (HierarchicalController)network;
							frame.updateGraphs(values, c.outputs);
						} catch(Exception e){
							frame.updateGraphs(values, outputs);
						}
					}
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
		
		try{
			bluetooth.disconnect();
		}catch(Exception e){}
	}
	
	public void setFrame(ControllerFrame frame) {
		this.frame = frame;
	}
	
	public void setExperiment(RemoteExperiment exp) {
		this.experiment = exp;
	}
	
	public void setRobot(Robot r) {
		this.robot = r;
	}
	
	public void setVirtualInputs(double[] values) {
		this.virtualInputs = values;
	}
}