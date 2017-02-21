
package org.usfirst.frc.team5700.robot.subsystems;


import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrainOld extends Subsystem {
	
	private RobotDrive drive;
	private SpeedController front_left_motor, back_left_motor,
	front_right_motor, back_right_motor;
	
	private ADXRS450_Gyro gyro;
	
	private Encoder rightEncoder;
	private Encoder leftEncoder;
	
//	private PIDController gyroControl;
//	private PIDOutput turnValue;
//	private double PIDAbsoluteTolerance;
	
	public DriveTrainOld() {
		super();
		front_left_motor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
		back_left_motor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
		front_right_motor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
		back_right_motor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
		
		
		//TODO RobotMap encoders
		double distancePerPulse = 6*Math.PI/360;
		leftEncoder = new Encoder(1, 2, false);
		rightEncoder = new Encoder(3, 4, true);
		leftEncoder.setDistancePerPulse(distancePerPulse);
		rightEncoder.setDistancePerPulse(distancePerPulse);
		leftEncoder.reset();
		rightEncoder.reset();
		
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		System.out.println("calibrating gyro.");
		gyro.reset();
		System.out.println("reset gyro.");
		System.out.println("gyro rate: " + gyro.getRate());
		
//		gyroControl = new pidController(0.1, 0.01, 0.001, gyro, df);
//		PIDCbsoluteTolerance = 3;
//		GyroControl.setAbsoluteTolerance(PIDAbsoluteTolerance);
	}
	
//	public void PIDGyroTurn(double turnAngle) {
//		gyroControl.setSetpoint(gyro.getAngle() + turnAngle);
//		drive.arcadeDrive(0, gyroControl.get());
//	}
	
//	public boolean gyroOnTarget() {
//		return gyroControl.onTarget();
//	}
	
	public void resetBothEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	public void resetGyroAngle() {
		gyro.reset();
	}
	
	/**
	 * gets gyro's angle
	 * @return
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
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
	/*
	 * Arcade Drive//
	 * Right joystick is for moving forwards and backwards
	 * Left joystick is for turning
	 * @nickmak
	 */
	public void arcadeDrive(Joystick left, Joystick right, boolean squaredInputs) {
		drive.arcadeDrive(-right.getY(), -left.getX(), squaredInputs);
	}
	
	/**
	 * Tank style driving for the DriveTrain.
	 * @param left Speed in range [-1,1]
	 * @param right Speed in range [-1,1]
	 */
	
	public void drive(double left, double right) {
		drive.tankDrive(left, right);
	}
	
	public double getLeftEncoderDistance() {
		return leftEncoder.getDistance();
	}
	
	public double getRightEncoderDistance() {
		return rightEncoder.getDistance();
	}
	
	public void stop() {
		drive(0,0);
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new ArcadeDriveWithJoysticks());
        //Set the default command for a subsystem here.
        //SetDefaultCommand(new MySpecialCommand());
    }
}
