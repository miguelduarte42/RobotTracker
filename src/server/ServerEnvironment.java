package server;

import java.util.LinkedList;

import tracking.Vector2d;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ServerEnvironment {
	
	public LinkedList<GroundPoint> objects = new LinkedList<GroundPoint>();
	private static int WIDTH = 10;
	
	public void addObjectCoordinates(Vector2d p) {
		Vector2d pixelPos = Translator.getPixelPosition(p);
		objects.add(new GroundPoint(pixelPos.x,pixelPos.y,p.x,p.y));
	}
	
	public void drawObjects(IplImage img) {
		for(GroundPoint p : objects) {
			cvLine(img, cvPoint((int)p.imageX, (int)p.imageY), cvPoint((int)p.imageX, (int)p.imageY), CvScalar.RED, WIDTH, CV_AA, 0);
		}
	}
	
	public void removeObject(Vector2d object) {	
		for(int i = 0 ; i < objects.size() ; i++) {
			if(object.x == objects.get(i).groundX && object.y == objects.get(i).groundY) {
				objects.remove(i);
			}
		}
	}

	public void removeAllObjects() {
		objects.clear();
	}

}