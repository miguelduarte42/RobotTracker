package client.epuck.behaviors;

import tracking.Robot;
import tracking.Vector2d;
import client.BluetoothHandler;
import client.epuck.RoboticController;
import client.epuck.WallButtonSensor;

public class PushButtonBehavior extends RoboticController {
	
	private BluetoothHandler bt;
	private boolean openedDoor = false;
	private Robot robot;
	private WallButtonSensor wallButtonSensor;
	
	public PushButtonBehavior(BluetoothHandler bt, WallButtonSensor wallButtonSensor, Robot robot) {
		super("Push Button");
		this.bt = bt;
		this.robot = robot;
		this.wallButtonSensor = wallButtonSensor;
	}

	@Override
	public double[] update(double[] inputs, int numberOutputs) {
		System.out.print(">> "+getName()+" ");
		double[] outputs = new double[numberOutputs];
		
		outputs[0] = 0.5;
		outputs[1] = 0.5;
		
		if(openedDoor || !wallButtonSensor.enabled)
			return outputs;
		
		Vector2d r = new Vector2d(robot.x,robot.y);
		if(r.distanceTo(wallButtonSensor.buttonPositions[0]) <= 30 ||
			r.distanceTo(wallButtonSensor.buttonPositions[1]) <= 30) {
			System.out.print("(awesome!)");
			openedDoor = true;
			
			try {
				bt.sendInts(new int[]{115});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			System.out.print("(Tried to open, but failed!)");
		
		return outputs;
	}

	@Override
	public void reset() {
		openedDoor = false;
	}
}