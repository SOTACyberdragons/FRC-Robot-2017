package org.usfirst.frc.team5700.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class BBLocator {
	
	NetworkTable networkTable;
	
	double[] defaultValue = new double[0];
	double[] coordinates = new double[4];
	double width, height, distanceFromBottom;
	
	double frameHeight = 786,
			frameWidth = 1280,
			angleOfView = 120;
	double aspectRatio = frameWidth/frameHeight;
	
	int xTopLeft = 0, 
		yTopLeft = 1, 
		xBottomRight = 2, 
		yBottomRight = 3;
	
	public BBLocator() {
		super();
		networkTable = NetworkTable.getTable("Vision");
		coordinates = networkTable.getNumberArray("BBCoordinates", defaultValue);
	}
	
	/**
	 * real angle = arctan((2*pixels from center*tan(half of angle of view))/width of frame)
	 * @param Number of pixels from the center of the frame
	 * @return Angle between center of the object found and center of camera
	 */
	public double getAngleFromHeading() {
		double pixelsFromCenter = getBBPixelsFromCenter();
		double angle;
		angle = Math.atan((2 * pixelsFromCenter * Math.tan(angleOfView/2))/frameWidth);
		return angle;
	}
	
	/**
	 * @return is the vision program sees an object
	 */
	public boolean seesObject() {
		return coordinates.length != 6;
	}
	
	/**
	 * @return center X pixel
	 */
	public double getBBCenterXPixel() {
		double centerX;
		centerX = (coordinates[xTopLeft] - coordinates[xBottomRight])/2;
		return centerX;
	}
	
	/**
	 * @return number of pixels from bottom of image to bottom of bounding box
	 */
	public double getPixelsFromBottom() {
		double dFromBottom = frameHeight - (frameHeight - coordinates[yBottomRight]);
		return dFromBottom;
	}
	
	/**
	 * @return center Y pixel
	 */
	public double getBBCenterYPixel() {
		double centerY;
		centerY = (coordinates[yTopLeft] - coordinates[yBottomRight])/2;
		return centerY;
	}
	
	/**
	 * @return height of bounding box
	 */
	public double getBBPixelHeight() {
		double height;
		height = coordinates[xTopLeft] - coordinates[xBottomRight];
		return height;
	}
	
	/**
	 * @return width of bounding box
	 */
	public double getBBPixelWidth() {
		double width;
		width = coordinates[xTopLeft] - coordinates[xBottomRight];
		return width;
	}
	
	/**
	 * @return number of pixels from the center of the frame
	 */
	public double getBBPixelsFromCenter() {
		double distance;
		distance = getBBCenterXPixel() - frameWidth/2;
		return distance;
	}
}
