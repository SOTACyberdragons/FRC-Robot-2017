/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.auto;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotDimensions;

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

	public TurnRadiusToAngle(double radius, double angle, double speed) {
		requires(Robot.drivetrain);
		
		this.targetAngleDeg = angle;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.drivetrain.drive(turnSpeed, Math.exp(-turnRadiusIn / RobotDimensions.WHEELBASE_IN));
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Robot.drivetrain.getHeading() >= targetAngleDeg;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.drivetrain.reset();
	}
	
	protected void interrupted() {
		end();
	}
}
