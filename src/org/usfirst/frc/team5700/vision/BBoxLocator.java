package org.usfirst.frc.team5700.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BBoxLocator {
	
	NetworkTable networkTable;
	
	double[] defaultValue = new double[0];
	double width, height, distanceFromBottom;
	
	static final double frameHeight = 480,
			frameWidth = 640,
			angleOfView = 60; //degrees
	
	static final double aspectRatio = frameWidth/frameHeight;
	
	static final int xTopLeftIndex = 0,
			yTopLeftIndex = 1,
			xBottomRightIndex = 2,
			yBottomRightIndex = 3;

	private static final double MAX_BBOX_SIZE = 180;
	
	public class Angle {
		public double angle;
		
		public Angle(double angle) {
			this.angle = angle;
		}
	}
	
	public BBoxLocator() {
		networkTable = NetworkTable.getTable("vision");
	}
	
	/**
	 * real angle = arctan((2*pixels from center*tan(half of angle of view))/width of frame)
	 * @param Number of pixels from the center of the frame
	 * @return Angle between center of the object found and center of camera, in degrees
	 */
	public Angle getAngleFromHeading() {
		double[] coordinates = networkTable.getNumberArray("BBoxCoordinates", defaultValue);
		SmartDashboard.putString("coordinate array", coordinates.toString());
		
		if (coordinates == null || coordinates.length != 6)
			return null;
		
		double centerX = (coordinates[xBottomRightIndex] + coordinates[xTopLeftIndex])/2;
		SmartDashboard.putNumber("centerX", centerX);
		
		if (Math.abs(coordinates[xBottomRightIndex] - coordinates[xTopLeftIndex]) > MAX_BBOX_SIZE)
			return null;
		
		double pixelsFromCenter = centerX - frameWidth/2;
		SmartDashboard.putNumber("pixelsFromCenter", pixelsFromCenter);
		
		double angle = (180/Math.PI) * Math.atan((pixelsFromCenter * Math.tan((angleOfView * Math.PI/180)/2))/(frameWidth/2));
		return new Angle(angle);
	}
}
