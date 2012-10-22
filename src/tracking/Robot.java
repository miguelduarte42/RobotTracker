package tracking;

import java.io.Serializable;


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
