/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.Dimensions;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class TurnRadiusToAngle extends Command {
	
	private double targetAngleDeg;
	private double turnSpeed;
	private double turnRadiusIn;
	private boolean turnLeft;
	private int turnDirection;
	private boolean useRecordedAngle;

	public TurnRadiusToAngle(double radius, double angle, double speed, boolean left) {
		requires(Robot.drivetrain);
		
		this.targetAngleDeg = angle;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
		this.turnLeft = left;
	}
	
	/**
	 * Take recorded angle
	 */
	public TurnRadiusToAngle(double radius, double speed, boolean left) {
		requires(Robot.drivetrain);
		
		this.useRecordedAngle = true;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
		this.turnLeft = left;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		if (useRecordedAngle) {
			targetAngleDeg = Robot.drivetrain.getRecordedAngle();
		}
				
		System.out.println();
		System.out.println("Initiate Blind Turn at Radius to Angle");
		System.out.println("Turn Radius: " + turnRadiusIn);
	    	System.out.println("Turn Angle: " + targetAngleDeg);
	    	System.out.println("Drive Speed: " + turnSpeed);
	    	System.out.println("Do Turn Left?: " + turnLeft);
    	
		Robot.drivetrain.reset();
		
		//TODO possibly wrong
		turnDirection = turnLeft ? 1 : -1;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.drivetrain.drive(turnSpeed, turnDirection * Math.exp(-turnRadiusIn / Dimensions.WHEELBASE_IN));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.drivetrain.getHeading()) >= targetAngleDeg;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.drivetrain.recordAngle();
		System.out.println("Blind Angle Record: " + Robot.drivetrain.getRecordedAngle());
		Robot.drivetrain.reset();
		System.out.println("Turn Radius To Angle Command Complete");
	}
	
	protected void interrupted() {
		end();
	}
}
