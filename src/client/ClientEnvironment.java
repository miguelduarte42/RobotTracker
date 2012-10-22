package client;

import java.util.LinkedList;
import tracking.Vector2d;

/**
 * Keeps a reference to the virtual objects that the
 * robot might need to interact with.
 * 
 * @author miguelduarte
 *
 */
public class ClientEnvironment {
	
	public LinkedList<Vector2d> objects = new LinkedList<Vector2d>();
	
	public void addObject(Vector2d p) {
		objects.add(p);
	}
	
	public LinkedList<Vector2d> removeObject(Vector2d robot, int range) {
		System.out.println("Remove Object!");
		
		LinkedList<Vector2d> objs = new LinkedList<Vector2d>();
		
		for(int i = 0 ; i < objects.size() ; i++)
			if(distanceBetween(robot, objects.get(i)) <= range) {
				System.out.println("Found object for removal");
				objs.add(objects.get(i));
				objects.remove(i);
			}
		
		return objs;
	}
	
	public int numberOfPreys() {
		return objects.size();
	}
	
	private double distanceBetween(Vector2d p1, Vector2d p2) {
		return Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
	}

}
