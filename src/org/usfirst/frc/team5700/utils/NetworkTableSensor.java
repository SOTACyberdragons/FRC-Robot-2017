package org.usfirst.frc.team5700.utils;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTableSensor implements PIDSource {
	
	NetworkTable table = Robot.networkTable;
	PIDSourceType source = PIDSourceType.kDisplacement;

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		//source = pidSource;

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return source;
	}

	public double pidGet() {
		double[] defaultValue = new double[0];
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		//return center of frame if not found
		double result = centerX.length < 1 ? 0 : - (centerX[0] - RobotMap.CAMERA_WIDTH / 2.0) * RobotMap.ANGLE_PER_PIXEL;
		System.out.println("NetworkTableSensor pidGet() says: " + result);
		return result;
	}
}
