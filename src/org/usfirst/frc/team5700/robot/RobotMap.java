package org.usfirst.frc.team5700.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//PWM
	public static final int BACK_LEFT_DRIVE_MOTOR = 0;
	public static final int BACK_RIGHT_DRIVE_MOTOR = 2;
	public static final int FRONT_LEFT_DRIVE_MOTOR = 1;
	public static final int FRONT_RIGHT_DRIVE_MOTOR = 3;
	
	public static final int CLIMB_MOTORS = 5;
	
	public static final int GEAR_INTAKE = 4;
	
	//DIO
	public static final int PEG_SWITCH = 5;
	
	//PDP Channels
	public static final int INTAKE_PDP = 0; //TODO find this
}