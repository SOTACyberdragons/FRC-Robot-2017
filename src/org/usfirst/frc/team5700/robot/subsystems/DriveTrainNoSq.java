
package org.usfirst.frc.team5700.robot.subsystems;


import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.TankDriveWithJoysticksNoSq;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrainNoSq extends Subsystem {
	
	private RobotDrive drive;
	private SpeedController front_left_motor, back_left_motor,
	front_right_motor, back_right_motor;
	
	public DriveTrainNoSq() {
		super();
		front_left_motor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
		back_left_motor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
		front_right_motor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
		back_right_motor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
	}

	
	/**
	 * Uses inputs from two joysticks to drive tank
	 * Joystick inputs are reversed
	 * @param left left joystick
	 * @param right right joystick
	 * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
	 */
	public void tankDriveNoSq(Joystick left, Joystick right) {
		drive.tankDrive(-left.getY(), -right.getY());
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
    	setDefaultCommand(new TankDriveWithJoysticksNoSq());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

}