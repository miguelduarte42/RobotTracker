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
	
	private RoboticController network;
	private BluetoothHandler bluetooth;
	private  int MAX_SPEED = 650;
	public int INPUTS = 4+8+4;
	public int OUTPUTS = 2;
	private static double SENSORS = 114, WHEEL_LEFT = 112, WHEEL_RIGHT = 113, START = 110;
	public boolean sensor = false;
	private ControllerFrame frame;
	private double[] virtualInputs = new double[INPUTS];
	private Robot robot;
	private boolean moveRobot = true;
	public int timestep = 0;
	private RemoteExperiment experiment;
	private int TIME = 100;
	
	private boolean useLight = false;
	
	public Controller(BluetoothHandler bt, RoboticController network) {
		this.network = network;
		this.bluetooth = bt;
	}
	
	public Controller(BluetoothHandler bt, RoboticController network,int inputs, int outputs) {
		INPUTS = inputs;
		OUTPUTS = outputs;
		virtualInputs = new double[INPUTS];
		this.network = network;
		bluetooth = bt;
	}
	
	public Controller(RoboticController network, ControllerFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void run() {
		
		int lag = 0;

		try {
			timestep = 0;
			
			bluetooth.connect();
			bluetooth.sendInts(new int[]{(int)START,(int)START});
			
			long time = 0;
			int lagTime = 0;
			
			while(true) {
				
				timestep++;
				time = System.currentTimeMillis();
				
				int inputs[] = bluetooth.readInts(1+(useLight ? 4+2 : 4));
				double values[] = new double[INPUTS];
				
				for(int i = 0 ; i < virtualInputs.length ; i++)
					values[i] = virtualInputs[i];
				
				if(inputs[0] == SENSORS) {
					
					for(int i = 1 ; i < inputs.length ; i++) {
						values[i-1] = ((double)inputs[i])/1000.0;
						if(i==1 || i == 4)
							values[i-1]*=0.8;
					}
					
					double[] outputs = network.update(values, this.OUTPUTS);
					System.out.println();
					
					int actuatorValues[] = new int[2*2];
					
					actuatorValues[0] = (int)WHEEL_LEFT;
					actuatorValues[1] = (int)((outputs[0]*2.0 - 1.0)*MAX_SPEED)+1000;
					actuatorValues[2] = (int)WHEEL_RIGHT;
					actuatorValues[3] = (int)((outputs[1]*2.0 - 1.0)*MAX_SPEED)+1000;
					
					boolean debug = false;
					
					if(debug) {
						actuatorValues[1] = 1000;
						actuatorValues[3] = 1000;
					}
					
					//pick prey
					if(outputs.length > 2 && outputs[2] > 0.5) {
						experiment.removeObject(robot.getPosition(),robot.preyPickRange);
						actuatorValues[1] = 1000;
						actuatorValues[3] = 1000;
					}
					
					if(!moveRobot) {
						actuatorValues[1] = 1000;
						actuatorValues[3] = 1000;
					}

					time = System.currentTimeMillis()-time;
					
					if(time > 5000) {
						System.out.println("#PUSH BUTTON "+time);
						time = 99;
					}
					
					if(time < TIME) {
						Thread.sleep(TIME-time);
					} else {
						System.err.println("LAG! "+time);
						lag++;
						lagTime+=time;
					}
					
					String status = experiment.preysPicked+" "+lag+"/"+timestep;
					
					if(lag > 0)
						status+=" (avg "+(lagTime/lag)+")";
					
					System.out.print(status);
					
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

	public int getCurrentStep() {
		return timestep;
	}
}