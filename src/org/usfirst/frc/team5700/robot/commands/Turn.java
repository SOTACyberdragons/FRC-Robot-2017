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
 * Turn using gyro PID.
 */
public class Turn extends Command {
	private PIDController pidAngle;
	private double driveOutput = 0;
	private double driveCurve = 1;

	private double angleKp = 0.01;
	private double angleKi = 0.001;
	private double angleKd = 0;
	
	private LinearAccelerationFilter filter;


	public Turn(double angle) {
		requires(Robot.drivetrain);
		
		pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveOutput = c;
			}
		});
		
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setAbsoluteTolerance(0.5);
		pidAngle.setSetpoint(angle);
		
		LiveWindow.addActuator("Turn", "Angle controller", pidAngle);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
		System.out.println("Turn initialize");
		
		
    	Preferences prefs = Preferences.getInstance();
		double filterSlopeTime = prefs.getDouble("Turn Filter Slope Time", 0.2);
		filter = new LinearAccelerationFilter(filterSlopeTime);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutput * filter.output(), driveCurve);
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
		
		System.out.println("Turn ended");
	}
}
