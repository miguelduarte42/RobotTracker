package client.epuck;

import java.util.LinkedList;

import tracking.GeometricInfo;
import tracking.Robot;
import tracking.Vector2d;

/**
 * Virtual sensor that detects virtual objects
 * in the environment.
 * 
 * @author miguelduarte
 *
 */
public class PreySensor {
	
	private int numberOfSensors = 8;
	private double openingAngle = Math.toRadians(90);
	private double range = 30;
	private double[] readings;
	private double[] angles;
	private double r;
	private Robot robot;
	
	public PreySensor(Robot robot) {
		this.robot = robot;
		r=(Math.PI/2)/openingAngle;
		
		double delta = 2.0 * Math.PI / (double)numberOfSensors;
		double angle = 0;
		
		this.readings = new double[numberOfSensors];
		this.angles = new double[numberOfSensors];
	
		for (int i = 0 ; i < numberOfSensors ; i++){
			angles[i] = angle;
			angle+=delta;
		}
	}
	
	public double[] update(LinkedList<Vector2d> objectsRealCoordinates) {
		
		for(int j = 0; j < numberOfSensors; j++)
			readings[j] = 0;
		
		for(Vector2d v : objectsRealCoordinates)
			for(int j = 0; j < numberOfSensors; j++)
				readings[j] = Math.max(calculateContributionToSensor(j, v),readings[j]);
		
		return readings;
	}
	
	private double calculateContributionToSensor(int sensorNumber,
			Vector2d source) {
		GeometricInfo sensorInfo = getSensorGeometricInfo(sensorNumber, source);
		
//		if(sensorNumber==0) {
//			System.out.println(sensorInfo.getAngle());
//		}
		
		if(sensorInfo.getDistance() < range && (sensorInfo.getAngle() < (openingAngle)) &&
				(sensorInfo.getAngle() > (-openingAngle))){

			//double currentMaxDistance = maxDistance - source.getObject().getRadius();
			
//			double distance=Math.max(sensorInfo.getDistance(),MIN_DISTANCE);	
			//return (currentMaxDistance - distance) / currentMaxDistance * (openingAngle - Math.abs(sensorInfo.getAngle())) / openingAngle;
				
//			double val=1/distance*Math.cos((sensorInfo.getAngle())*r)/MIN_READING;
//			return val;
			double val = ((range-sensorInfo.getDistance())/range)*Math.cos((sensorInfo.getAngle())*r);// + simulator.getRandom().nextGaussian() * NOISESTDEV;
			if (val > 1.0)
				val = 1.0;
			else if (val < 0.0)
				val = 0.0;
			
			return val;
		}
 		return 0;
	}

	private GeometricInfo getSensorGeometricInfo(int sensorNumber, Vector2d source) {
		double orientation = angles[sensorNumber] + robot.orientation;
//			System.out.println(orientation+" = "+angles[sensorNumber]+" + "+robot.orientation);
//		sensorPosition.set(Math.cos(orientation)*robot.getRadius()+robot.getPosition().getX(),
//				Math.sin(orientation)*robot.getRadius()+robot.getPosition().getY());
		Vector2d sensorPosition = new Vector2d(robot.x, robot.y);
		GeometricInfo sensorInfo = getGeometricInfoBetweenPoints(sensorPosition, orientation, source, sensorNumber);
		
		return sensorInfo;
	}
	
	private GeometricInfo getGeometricInfoBetweenPoints(Vector2d fromPoint, double orientation,
			Vector2d toPoint, int sensorNumber){
		Vector2d light = toPoint;
		Vector2d lightDirection = new Vector2d();
		lightDirection.set(light.getX() - fromPoint.getX(), light.getY() - fromPoint.getY());
		
		if(orientation>Math.PI){
			orientation-=2*Math.PI;
		} else if(orientation<-Math.PI){ 
			orientation+=2*Math.PI;
		}
		
		double lightAngle = orientation + lightDirection.getAngle();
		
//		if(sensorNumber == 0)
//			System.out.println(lightAngle+" = "+orientation+" + "+lightDirection.getAngle());

		if(lightAngle>Math.PI){
			lightAngle-=2*Math.PI;
		} else if(lightAngle<-Math.PI){ 
			lightAngle+=2*Math.PI;
		}
		
		return new GeometricInfo(lightAngle, lightDirection.length());
	}
}
