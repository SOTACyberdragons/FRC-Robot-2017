/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class FollowImage extends Command {
	private PIDController pidAngle;
	private double driveOutput = 0;

	private double angleKp = 0.01;
	private double angleKi = 0.001;
	private double angleKd = 0;
	private double autoPower;
	
	private LinearAccelerationFilter filter;


	public FollowImage(double distance) {
		pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.networkTableSensor,
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveOutput = c;
			}
		});
		
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setAbsoluteTolerance(0.5);//TODO determine units
		pidAngle.setSetpoint(0);
		
		LiveWindow.addActuator("Drive", "Angle controller", pidAngle);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
		System.out.println("FollowImage initialize");
		
		
    	Preferences prefs = Preferences.getInstance();
		double filterSlopeTime = prefs.getDouble("Filter Slope Time", 0.5);
		autoPower = prefs.getDouble("Auto Power", 1.0);
		filter = new LinearAccelerationFilter(filterSlopeTime);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutput * filter.output() * autoPower, 1);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return pidAngle.onTarget();
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {

		// Stop PID and the wheels

		pidAngle.disable();
		Robot.drivetrain.drive(0, 0);
		
		Robot.drivetrain.reset();
		pidAngle.reset();
		
		System.out.println("FollowImage ended");
	}
}
