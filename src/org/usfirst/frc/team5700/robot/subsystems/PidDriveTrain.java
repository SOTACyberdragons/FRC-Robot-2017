
package org.usfirst.frc.team5700.robot.subsystems;


import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class PidDriveTrain extends PIDSubsystem {
	
	private RobotDrive drive;
	private SpeedController front_left_motor, back_left_motor,
	front_right_motor, back_right_motor;
	
	private Encoder rightEncoder;
	private Encoder leftEncoder;

	
	public PidDriveTrain() {
		super("PidDriveTraind", 1.0, 0.0, 0.0);
		LiveWindow.addActuator("PidDriveTrain", "PidSubsystemController", getPIDController());
		front_left_motor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
		back_left_motor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
		front_right_motor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
		back_right_motor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
		
		double distancePerPulse = 6*Math.PI/360;
		leftEncoder = new Encoder(1, 2, false);
		rightEncoder = new Encoder(3, 4, true);
		leftEncoder.setDistancePerPulse(distancePerPulse);
		rightEncoder.setDistancePerPulse(distancePerPulse);
	}

	
	public double getLeftEncoderDistance() {
		return leftEncoder.getDistance();
	}
	
	public double getRightEncoderDistance() {
		return rightEncoder.getDistance();
	}

    public void initDefaultCommand() {
    }

	@Override
	protected double returnPIDInput() {
		//For now, returning distance from a single encoder
		//Later could switch to average of the two
		return rightEncoder.getDistance();
	}

	@Override
	protected void usePIDOutput(double output) {
		//send input to arcade drive
		drive.arcadeDrive(output, 0.0);
		
	}
}
