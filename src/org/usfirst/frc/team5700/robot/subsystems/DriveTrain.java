
package org.usfirst.frc.team5700.robot.subsystems;


import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.TankDriveWithJoysticks;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	
	private RobotDrive drive;
	private SpeedController front_left_motor, back_left_motor,
	front_right_motor, back_right_motor;
	
	//ultrasonic sensor
	private AnalogInput ultrasonicSensor;
	
	public DriveTrain() {
		super();
		front_left_motor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
		back_left_motor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
		front_right_motor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
		back_right_motor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
		
		ultrasonicSensor = new AnalogInput(0);
	}

	public double getUltrasonicSensorVoltage() {
		return ultrasonicSensor.getVoltage();
	}
	
	public double getRangefinderDistanceInFeet() {
		return ultrasonicSensor.getVoltage()/0.117192;
	}

				
	/**
	 * Uses inputs from two joysticks to drive tank
	 * Joystick inputs are reversed
	 * @param left left joystick
	 * @param right right joystick
	 * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
	 */
	public void tankDrive(Joystick left, Joystick right, boolean squaredInputs) {
		drive.tankDrive(-left.getY(), -right.getY(), squaredInputs);
	}
	
	/**
	 * Tank style driving for the DriveTrain.
	 * @param left Speed in range [-1,1]
	 * @param right Speed in range [-1,1]
	 */
	
	public void drive(double left, double right) {
		drive.tankDrive(left, right);
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new TankDriveWithJoysticks());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
