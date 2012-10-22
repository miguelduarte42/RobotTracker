package tracking;

import java.io.Serializable;

/**
 * 
 * Simple class that will be transmitted between the Server
 * and the Client to indicate the current position & orientation
 * of the robot. Although it's not pretty, I also use it to
 * tell the Server to add or remove objects, using the orientation
 * as a boolean value (0 or 1).
 * 
 * @author miguelduarte
 */
public class Robot implements Serializable,Cloneable {
	
	public double orientation = 0;
	public double x = 0;
	public double y = 0;
	public int preySensorRange = 30;
	public int preyPickRange = 15;
	
	public void setPosition(Vector2d p) {
		x = p.x;
		y = p.y;
	}

	public Vector2d getPosition() {
		return new Vector2d(x,y);
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
