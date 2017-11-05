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
public class TurnRadiusPastAngle extends Command {
	
	private double targetAngleDeg;
	private double turnSpeed;
	private double turnRadiusIn;
	private enum Direction {LEFT, RIGHT};
	private int turnDirection;
	private boolean useRecordedAngle;
	private boolean recordAngle = false;

	/**
	 * No PID
	 * @param radius
	 * @param angle
	 * @param speed
	 * @param left
	 */
	public TurnRadiusPastAngle(double radius, double angle, double speed, boolean turnLeft, boolean recordAngle) {
		requires(Robot.drivetrain);
		
		this.targetAngleDeg = angle;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
		this.turnDirection = turnLeft ? 1 : -1;
		this.recordAngle = recordAngle;
	}
	
	/**
	 * Use recorded angle
	 */
	public TurnRadiusPastAngle(double radius, double speed, boolean turnLeft) {
		requires(Robot.drivetrain);
		
		this.useRecordedAngle = true;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
		this.turnDirection = turnLeft ? 1 : -1;
	}

	@Override
	protected void initialize() {

		if (useRecordedAngle) {
			this.targetAngleDeg = Robot.drivetrain.getRecordedAngle();
			System.out.println("Using recorded angle");
		} else {
			System.out.println("Using preset angle");
		}
		
		//logs
		System.out.println();
		System.out.println("TurnRadiusPastAngle Initiated");
		System.out.println("  Turn Radius: " + turnRadiusIn);
	    	System.out.println("  Turn Angle: " + targetAngleDeg + " Degrees");
	    	System.out.println("  Drive Speed: " + turnSpeed);
	    	System.out.println("  Turn Direction (1 left, -1 right: " + turnDirection);
    	
		Robot.drivetrain.reset();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(turnSpeed, turnDirection * Math.exp(-turnRadiusIn / Dimensions.WHEELBASE_IN));
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.drivetrain.getHeading()) >= targetAngleDeg;
	}

	@Override
	protected void end() {
		if (recordAngle) {
			Robot.drivetrain.recordAngle();
			System.out.println("Recorded angle: " + Robot.drivetrain.getRecordedAngle());
		}
		
		Robot.drivetrain.reset();
		System.out.println("Turn Radius To Angle Command Complete");
	}
	
	protected void interrupted() {
		end();
	}
}
