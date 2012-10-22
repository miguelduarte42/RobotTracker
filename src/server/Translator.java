package server;

import tracking.Vector2d;

/**
 * Converts a pixel coordinate to a real-world coordinate,
 * or vice-versa.
 * 
 * @author miguelduarte
 *
 */
public class Translator {
	
	public static GroundPoint[][] points = {
			{//Top
				new GroundPoint(6,230,0,120),
				new GroundPoint(173,227,30,120),
				new GroundPoint(337,222,60,120),
				new GroundPoint(499,217,90,120),
				new GroundPoint(655,217,120,120),
				new GroundPoint(810,215,150,120),
				new GroundPoint(961,211,180,120),
				new GroundPoint(1113,210,210,120),
			},
			{//Right
				new GroundPoint(1132,818,210,0),
				new GroundPoint(1127,668,210,30),
				new GroundPoint(1122,515,210,60),
				new GroundPoint(1119,363,210,90),
				new GroundPoint(1113,210,210,120)
			},
			{//Bottom
				new GroundPoint(35,880,0,0),
				new GroundPoint(200,870,30,0),
				new GroundPoint(365,860,60,0),
				new GroundPoint(523,850,90,0),
				new GroundPoint(680,840,120,0),
				new GroundPoint(833,833,150,0),
				new GroundPoint(985,827,180,0),
				new GroundPoint(1132,818,210,0)
			},
			{//Left
				new GroundPoint(6,230,0,120),
				new GroundPoint(14,395,0,90),
				new GroundPoint(22,555,0,60),
				new GroundPoint(27,720,0,30),
				new GroundPoint(35,880,0,0)
			}
	};
	
	public static Vector2d getRealPosition(Vector2d point) {
		
		GroundPoint top = getClosestReal(points[0], point, true);
		GroundPoint right = getClosestReal(points[1], point, false);
		GroundPoint down = getClosestReal(points[2], point, true);
		GroundPoint left = getClosestReal(points[3], point, false);
		
		double minY = top.imageY;
		double maxY = down.imageY;
		
		double minX = left.imageX;
		double maxX = right.imageX;
		
		double diffX = maxX - minX;
		double diffY = maxY - minY;
		
		double percentX = (point.x-minX)/diffX;
		double percentY = (maxY-point.y)/diffY;
		
		return new Vector2d(right.groundX*percentX,top.groundY*percentY);
	}
	
	public static Vector2d getPixelPosition(Vector2d point) {
		
		GroundPoint top = getClosestPixel(points[0], point, true);
		GroundPoint right = getClosestPixel(points[1], point, false);
		GroundPoint down = getClosestPixel(points[2], point, true);
		GroundPoint left = getClosestPixel(points[3], point, false);
		
		double minY = top.groundY;
		double maxY = down.groundY;
		
		double minX = left.groundX;
		double maxX = right.groundX;
		
		double diffX = maxX - minX;
		double diffY = maxY - minY;
		
		double percentX = (point.x-minX)/diffX;
		double percentY = (point.y-minY)/diffY;
		
		double newX = percentX*(right.imageX-left.imageX)+left.imageX;
		double newY = percentY*(down.imageY-top.imageY)+top.imageY;
		
		return new Vector2d(newX,newY);
	}
	
	private static GroundPoint getClosestReal(GroundPoint[] points, Vector2d target, boolean x) {
		
		double min = Double.MAX_VALUE;
		GroundPoint closest = null;
		
		for(GroundPoint p : points) {
			double diff = x ? Math.abs(p.imageX-target.x) : Math.abs(p.imageY-target.y);
			if(diff < min) {
				min = diff;
				closest = p;
			}
		}
		
		return closest;
	}
	
	private static GroundPoint getClosestPixel(GroundPoint[] points, Vector2d target, boolean x) {
		
		double min = Double.MAX_VALUE;
		GroundPoint closest = null;
		
		for(GroundPoint p : points) {
			double diff = x ? Math.abs(p.groundX-target.x) : Math.abs(p.groundY-target.y);
			if(diff < min) {
				min = diff;
				closest = p;
			}
		}
		
		return closest;
	}

}
