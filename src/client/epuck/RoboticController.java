package client.epuck;

public abstract class RoboticController {
	
	private boolean isLocked = false;
	private String name;
	
	public RoboticController(String name) {
		this.name = name;
	}
	
	public abstract double[] update(double[] inputs, int numberOutputs);
	public abstract void reset();

	public boolean isLocked() {
		return isLocked;
	}
	
	public String getName() {
		return name;
	}
}
