package server;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import java.awt.Dimension;

import tracking.Robot;
import tracking.Vector2d;

/**
 * Main class for the Location Server. It initiates the server
 * and the video tracker to get the robot's location and orientation.
 * 
 * @author miguelduarte
 *
 */
public class TrackingSystem extends Thread{
	
	private Video video;
	private ServerEnvironment environment;
	private Robot robot = new Robot();
	private LocationServer server;
	private IplImage smallImg;
	private static int WIDTH = 600;
	private static int HEIGHT = 400;
	
	public TrackingSystem() {
		video = new Video(false);
		environment = new ServerEnvironment();
		server = new LocationServer(this);
	}
	
	@Override
	public void run() {
		
		video.start();
		server.start();
		CanvasFrame frame = new CanvasFrame("Tracking System");
		frame.setDefaultCloseOperation(CanvasFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		smallImg = cvCreateImage(cvSize(WIDTH,HEIGHT), 8, 3);
		
		while(true) {
			try {
				
				Vector2d robotPosition = pointToVector(video.robotPosition);
				Vector2d realPosition = Translator.getRealPosition(robotPosition);
				
				robot.setPosition(realPosition);
				robot.orientation = Math.toRadians(video.robotOrientation);
				
				if(video.currentImage != null) {
					environment.drawObjects(video.currentImage);
					
//					for(GroundPoint[] a : Translator.points) {
//						for(GroundPoint p : a)
//							cvLine(video.currentImage, cvPoint((int)p.imageX, (int)p.imageY), cvPoint((int)p.imageX, (int)p.imageY), CvScalar.GREEN, 5, CV_AA, 0);
//					}
					
					cvResize(video.currentImage, smallImg);
					frame.showImage(smallImg);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private Vector2d pointToVector(CvPoint p) {
		return new Vector2d(p.x(),p.y());
	}
	
	public static void main(String[] args) {
		new TrackingSystem().start();
	}
	
	public IplImage getImage() {
		return smallImg;
	}
	
	public Robot getRobot() {
		return robot;
	}
	
	public ServerEnvironment getEnvironment() {
		return environment;
	}

}
