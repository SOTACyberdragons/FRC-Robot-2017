package org.usfirst.frc.team5700.robot;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	 //Climbing motors

	public static final int BACK_LEFT_DRIVE_MOTOR = 3;
	public static final int BACK_RIGHT_DRIVE_MOTOR = 1;
	public static final int FRONT_LEFT_DRIVE_MOTOR = 4;
	public static final int FRONT_RIGHT_DRIVE_MOTOR = 2;
	
	public static final int CLIMB_MOTORS = 0;
	
	public static final int GEAR_TRIGGER = 0;
	
	
	//Microsoft Lifecam3000
	public static final int CAMERA_WIDTH = 160;
	public static final int CAMERA_HEIGHT = 90;
	//diagonal field of view in degrees
	public static final double CAMERA_DIAGONAL_FOV = 68.5;
	//vertical field of view
	public static final double ANGLE_PER_PIXEL = CAMERA_DIAGONAL_FOV/sqrt(pow(CAMERA_WIDTH,2) + pow(CAMERA_HEIGHT,2));
}