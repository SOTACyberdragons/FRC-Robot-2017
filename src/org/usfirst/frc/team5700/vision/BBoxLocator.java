package org.usfirst.frc.team5700.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BBoxLocator {
	
	NetworkTable networkTable;
	
	double[] defaultValue = new double[0];
	double width, height, distanceFromBottom;

	private double objectWidthIn;
	
	static final double frameHeight = 480,
			frameWidth = 640,
			ANGLE_OF_VIEW_DEG = 60, //degrees
			ANGLE_OF_VIEW = ANGLE_OF_VIEW_DEG * Math.PI/180;
	
	static final double aspectRatio = frameWidth / frameHeight;
	
	static final int xTopLeftIndex = 0,
			yTopLeftIndex = 1,
			xBottomRightIndex = 2,
			yBottomRightIndex = 3;

	private static final double ROBOT_CENTER = 8.0;
	
	public class BBox {
		public double angleDeg;
		public double distanceIn;
		public double widthPx;
		
		public BBox(double widthPx, double angleDeg, double distanceIn) {
			this.widthPx = widthPx;
			this.angleDeg = angleDeg;
			this.distanceIn = distanceIn;
		}
	}
	
	public BBoxLocator(double objectWidthIn) {
		networkTable = NetworkTable.getTable("vision");
		
		this.objectWidthIn = objectWidthIn;
	}
	
	/**
	 * real angle = arctan((2*pixels from center*tan(half of angle of view))/width of frame)
	 * @param Number of pixels from the center of the frame
	 * @return Angle between center of the object found and center of camera, in degrees
	 */
	public BBox getBBox() {
		double[] coordinates = networkTable.getNumberArray("BBoxCoordinates", defaultValue);
		SmartDashboard.putString("coordinate array", coordinates.toString());
		
		if (coordinates == null || coordinates.length != 6)
			return null;
		
		double centerX = (coordinates[xBottomRightIndex] + coordinates[xTopLeftIndex])/2;
		SmartDashboard.putNumber("centerX", centerX);
		
		double boxWidth = Math.abs(coordinates[xBottomRightIndex] - coordinates[xTopLeftIndex]);
		
		double pixelsFromCenter = centerX - frameWidth/2;
		SmartDashboard.putNumber("pixelsFromCenter", pixelsFromCenter);
		
		double cameraAngle = Math.atan((pixelsFromCenter * 
						Math.tan(ANGLE_OF_VIEW/2))
						/(frameWidth/2));
		
		double distanceIn = frameWidth / boxWidth / 2 / 
				Math.tan(ANGLE_OF_VIEW) * objectWidthIn;
		
		//fudge: 0.05 * d^2
		distanceIn = 0.05 * distanceIn * distanceIn;
		
		double angle = Math.atan(Math.sin(cameraAngle) / (ROBOT_CENTER / distanceIn + 
				Math.cos(cameraAngle)));
		
		double angleDeg = angle * 180 / Math.PI;
		double cameraAngleDeg = cameraAngle * 180 / Math.PI;
		SmartDashboard.putNumber("Vision Robot Angle Deg", angleDeg);
		SmartDashboard.putNumber("Vision Camera Angle Deg", cameraAngleDeg);
		SmartDashboard.putNumber("Vision Distance In", distanceIn);
		
		return new BBox(boxWidth, angleDeg, distanceIn);
	}
}
