package server;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.awt.Dimension;
import tracking.RobotKalman;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_highgui;

/**
 * Handles the robot tracking based on images from a camera.
 * In our setup, the camera is positioned in the ceiling at
 * around 3 meters. It covers a span of around 2.3 meters in
 * width and 1.5 meters in height. The robot has a marker on top,
 * with a diameter of 8 centimeters. The marker is a white circle
 * with a black cross. The arms of the cross have a width of roughly
 * 3 centimeters. An example of the marker can be seen here:
 * http://miguelduarte.pt/media/robot_marker.jpg
 * 
 * The parameters of the video processing algorithm might have to
 * be tweaked depending on the lighting or camera setup. The algorithm
 * is as follows:
 * 
 * 1) Value-based threshold to find the white circle, on a grayscale version
 * 		of the image
 * 2) Dilation of the image to "hide" the cross in the circle
 * 3) Erosion to return the circle to its original size, without the cross
 * 4) Canny to detect the edges of the circle
 * 5) Hough Circles to detect the center and radius of the circle
 * 6) Image crop to work on the circle area only
 * 7) Value-based threshold to find the cross
 * 8) Hough Lines to find all the lines in the cross
 * 9) Line intersection to find perpendicular lines
 * 10) Detection of longer line in order to assess the orientation
 * 
 * In order to increase performance, the image is cropped if the circle
 * was found on the previous cycle. From my measurements, it takes roughly
 * 3 miliseconds to process one frame, on a 3.0Ghz 8-core AMD processor.
 * 
 * This currently tracks only one robot.
 * 
 * @author miguelduarte
 *
 */
public class Video extends Thread{
	
	public CvPoint robotPosition = cvPoint(0,0);
	public double robotOrientation = 0;
	public int frames = 0;
	public int successAngle = 0;
	public int successCircle = 0;
	public IplImage currentImage;
    private CanvasFrame canvas;
    private boolean displayVideo = false;
    
    RobotKalman kalman = new RobotKalman();
    int widthRoi = 100;
    IplImage image;
    CvPoint intersection = cvPoint(0,0);
    CvPoint tail = cvPoint(0,0);
    CvPoint center = cvPoint(0,0);
    
    CvPoint estTail = cvPoint(0,0);
    CvPoint estIntersection = cvPoint(0,0);
    CvPoint estCenter = cvPoint(0,0);
    long startingTime = System.currentTimeMillis();
    
    boolean successfulIntersection = false;
    
    public Video(boolean displayVideo) {
		this.displayVideo = displayVideo;
		if(displayVideo) {
			 canvas = new CanvasFrame("Camera");
			 canvas.setPreferredSize(new Dimension(600, 400));
		}
	}
	
	@Override
	public void run() {
		CvCapture capture = opencv_highgui.cvCreateCameraCapture(0);

        opencv_highgui.cvSetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 960);
        opencv_highgui.cvSetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 1280);
        boolean successfulIntersection = false;
		while (true) {
			if((image = opencv_highgui.cvQueryFrame(capture)) == null) {
				break;
			} else {
				countFramesPerSecond();
				processImage();
			}
		}
	}
	
	private void processImage() {
		if(currentImage == null)
			currentImage = IplImage.create(cvGetSize(image), 8, 3);
		
		IplImage imageGray = null;
		CvRect roi = null;
		
		if(successfulIntersection) {
			roi = cvRect((int)center.x()-widthRoi/2, (int)center.y()-widthRoi/2, widthRoi, widthRoi);
			cvSetImageROI(image,roi);
		}
		
		imageGray = IplImage.create(cvGetSize(image), 8, 1);
		
		cvCvtColor(image, imageGray, CV_RGB2GRAY);
		
		CvMemStorage storage = cvCreateMemStorage(0);
			
		cvThreshold(imageGray, imageGray, 240, 255, CV_THRESH_BINARY);
	    cvDilate(imageGray, imageGray, null, 4);
	    cvErode(imageGray, imageGray, null, 5);
	    
//		cvSmooth(imageGray, imageGray, CV_GAUSSIAN, 3);
		cvCanny(imageGray, imageGray, 100, 100, 3);
		//Find circles
		CvSeq circles = cvHoughCircles( 
				imageGray, //Input image
				storage, //Memory Storage
			    CV_HOUGH_GRADIENT, //Detection method
			    1, //Inverse ratio
			    100, //Minimum distance between the centers of the detected circles
			    10, //Higher threshold for canny edge detector
			    15, //Threshold at the center detection stage
			    15, //min radius
			    30 //max radius
			    );
		
		if(successfulIntersection)
			cvResetImageROI(image);
		
		if(circles.total() == 1) {
			successCircle++;
			
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, 0));
			
			float x=circle.x();
			float y=circle.y();
			
			if(successfulIntersection) {
				x+=center.x()-widthRoi/2;
				y+=center.y()-widthRoi/2;
			}
			
			center = cvPoint((int)x,(int)y);
			
			int width = Math.round(circle.z())*2;
			
			//Crop image
			roi = cvRect((int)x-width/2, (int)y-width/2, width, width);
			cvSetImageROI(image,roi);
			IplImage tempImage = IplImage.create( cvSize(roi.width(), roi.height()), 8, 3);
			IplImage maskedImage = IplImage.create(cvSize(roi.width(), roi.height()), 8, 3);
			IplImage roiImg = IplImage.create( cvSize(roi.width(),roi.width()), 8, 1);
			
			cvCopy(image,tempImage);
			cvResetImageROI(image);
			
		    // prepare the 'ROI' image
		    cvZero(roiImg);
		 
		    cvCircle(
		        roiImg,
		        cvPoint(width/2, width/2),
		        (int)(circle.z()),
		        CV_RGB(255, 255, 255),
		        -1, 8, 0
		    );
		    
		    cvZero(maskedImage);
		    
		    // extract subimage
		    cvNot(tempImage,tempImage);
		    cvCopy(tempImage, maskedImage, roiImg);
		    
			IplImage thresholdedCross = IplImage.create(maskedImage.cvSize(),8,1);
			
			//Extract cross
			cvCvtColor(maskedImage, thresholdedCross, CV_BGR2GRAY);
			
			
			
			cvThreshold(thresholdedCross, thresholdedCross, 120, 255, CV_THRESH_BINARY);
//			cvSmooth(thresholdedCross, thresholdedCross, CV_GAUSSIAN, 3);
//			cvDilate(thresholdedCross,thresholdedCross,null,1);
			
			
			CvMemStorage storage2 = cvCreateMemStorage(0);
	        
	        CvSeq lines = cvHoughLines2(thresholdedCross, storage2, CV_HOUGH_PROBABILISTIC, 1, Math.PI / 180, 20, 15, 50);
	        int[] perpendicularLinesIndexes = getPerpendicularLines(lines);
	        
	        if(perpendicularLinesIndexes[1] != 0) {
	        	
	        	CvPoint inter =
	        		calculateIntersectionPoint(
	        			cvGetSeqElem(lines,perpendicularLinesIndexes[0]),
	        			cvGetSeqElem(lines,perpendicularLinesIndexes[1])
	        		);
	        	
	        	successfulIntersection = false;
	        	
	        	if(inter != null) {
	        		
	        		successfulIntersection = true;
	        		successAngle++; 
	        		
	        		intersection = inter;
	        		
	        		CvPoint pt1  = new CvPoint(cvGetSeqElem(lines,perpendicularLinesIndexes[0])).position(0);
		        	CvPoint pt2  = new CvPoint(cvGetSeqElem(lines,perpendicularLinesIndexes[0])).position(1);
		        	CvPoint pt3  = new CvPoint(cvGetSeqElem(lines,perpendicularLinesIndexes[1])).position(0);
		            CvPoint pt4  = new CvPoint(cvGetSeqElem(lines,perpendicularLinesIndexes[1])).position(1);
		            
		            CvPoint midpoint1 = cvPoint((pt1.x()+pt2.x())/2, (pt1.y()+pt2.y())/2); 
		            CvPoint midpoint2 = cvPoint((pt3.x()+pt4.x())/2, (pt3.y()+pt4.y())/2);
		            tail = null;
		            
		            if(distanceBetween(intersection,midpoint1) > distanceBetween(intersection,midpoint2)) {
		            	
		            	if(distanceBetween(pt1,intersection) > distanceBetween(pt2, intersection))
		            		tail = pt1;
		            	else
		            		tail = pt2;
		            } else {
		            	if(distanceBetween(pt3,intersection) > distanceBetween(pt4, intersection))
		            		tail = pt3;
		            	else
		            		tail = pt4;
		            }
		            
		            tail = cvPoint((int)(tail.x()+(x-width/2)),(int)(tail.y()+(y-width/2)));
				    intersection = cvPoint((int)(intersection.x()+(x-width/2)),(int)(intersection.y()+(y-width/2)));
				    
				   CvPoint[] estimation = kalman.getEstimation(tail,intersection,center);
		            
		           estTail = estimation[0];
		           estIntersection = estimation[1];
		           estCenter = estimation[2];
				    
		           robotPosition = cvPoint(estCenter.x(),estCenter.y());
	        	}
	        }else
	        	successfulIntersection = false;
	        
	        if(!successfulIntersection)
	        	robotPosition = cvPoint(center.x(),center.y());
            
           robotOrientation = cvFastArctan(estIntersection.y()-estTail.y(),estIntersection.x()-estTail.x());

           cvReleaseMemStorage(lines.storage());
           roiImg.release();
           tempImage.release();
           maskedImage.release();
           thresholdedCross.release();
			
		}else
			successfulIntersection = false;
		
		cvLine(image, estTail, estIntersection, CvScalar.YELLOW, 3, CV_AA, 0);
        cvLine(image, estCenter, estCenter, CvScalar.RED, 3, CV_AA, 0);
        cvLine(image, estTail, estTail, CvScalar.BLUE, 3, CV_AA, 0);
        cvLine(image, estIntersection, estIntersection, CvScalar.BLUE, 3, CV_AA, 0);
        
		cvCopy(image, currentImage);
		
		cvReleaseMemStorage(circles.storage());
		imageGray.release();
	}
			
	private void countFramesPerSecond() {
		
		double timeElapsed = (System.currentTimeMillis()-startingTime)/1000.0;
		
		if(frames > 100)
			System.out.println("fps: "+((frames-100)/timeElapsed +" "+(frames-100)));
		else
			startingTime = System.currentTimeMillis();
		
		frames++;
		
		if(frames > 200)
			frames = 99;
		
	}

	private void showImage(IplImage img) {
		if(displayVideo && img != null && canvas != null)
			canvas.showImage(img);
	}
	
	private double distanceBetween(CvPoint p1, CvPoint p2) {
		return Math.sqrt(Math.pow(p1.x()-p2.x(),2)+Math.pow(p1.y()-p2.y(),2));
	}
	
	private int[] getPerpendicularLines(CvSeq lines) {
		
		double[] angles = new double[2];
		int[] indexes = new int[2];
        int angleIndex = 0;
        
        for(int j = 0 ; j < lines.total() && angleIndex < 2 ; j++) {
        	
        	angles = new double[2];
        	indexes = new int[2];
        	angleIndex = 0;
        	
	        for (int i = j; i < lines.total(); i++) {
	
	            Pointer line = cvGetSeqElem(lines, i);
	            CvPoint pt1  = new CvPoint(line).position(0);
	            CvPoint pt2  = new CvPoint(line).position(1);
	            
	            double angle = cvFastArctan( pt2.y()-pt1.y(),pt2.x()- pt1.x());
	            
	            while(angle < 0)
	            	angle+=360;
	            
	            while(angle > 180)
	            	angle-=180;
	            
	            if(angleIndex == 0) {
	            	indexes[angleIndex] = i;
	            	angles[angleIndex++] = angle;
	            } else if(angleIndex == 1){
	            	
	            	double minAngle = Math.min(angles[0], angle);
	            	double maxAngle = Math.max(angles[0], angle);
	            	
	            	if(maxAngle - 90 < minAngle + 5 && maxAngle -90 > minAngle - 5) {
	            		indexes[angleIndex] = i;
	            		angles[angleIndex++] = angle;
	            		break;
	            	}
	            	
	            }
	        }
        }
        return indexes;		
	}
	
	private CvPoint calculateIntersectionPoint(Pointer line1, Pointer line2) {
		
        CvPoint p1  = new CvPoint(line1).position(0);
        CvPoint p2  = new CvPoint(line1).position(1);
        
        CvPoint p3  = new CvPoint(line2).position(0);
        CvPoint p4  = new CvPoint(line2).position(1);
		
		double x1 = p1.x(), x2 = p2.x(), x3 = p3.x(), x4 = p4.x();
		double y1 = p1.y(), y2 = p2.y(), y3 = p3.y(), y4 = p4.y();
		double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) 
			return null;
		double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		
		return cvPoint((int)xi,(int)yi);
	}

}
