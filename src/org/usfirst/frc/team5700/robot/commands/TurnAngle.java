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
public class TurnAngle extends Command {
	
	private PIDController pidAngle;
	private double turnSpeed = 0;
	private double angleDeg;

	private double angleKp;
	private double angleKi;
	private double angleKd;
	
	private LinearAccelerationFilter filter;


	public TurnAngle(double angleDeg) {
		requires(Robot.drivetrain);
		
		pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				turnSpeed = c;
			}
		});
		
		this.angleDeg = angleDeg;
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setSetpoint(angleDeg);
		
		LiveWindow.addActuator("drivetrain", "Turn Angle controller", pidAngle);
	}

	@Override
	protected void initialize() {
		//Get PID values from preferences
		Preferences prefs = Preferences.getInstance();
		angleKp = prefs.getDouble("TurnAngle Kp", 0.01);
		angleKi = prefs.getDouble("TurnAngle Ki", 0.001);
		angleKd = prefs.getDouble("TurnAngle Kd", 0.0);
		pidAngle.setAbsoluteTolerance(prefs.getDouble("TurnAngle Tol.", 4));
		
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
		
		double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.2);
		filter = new LinearAccelerationFilter(filterSlopeTime);
		
		//log
		System.out.println();
		System.out.println("TurnAngle Initialized");
		System.out.println("Turn to " + angleDeg + " Degrees");
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(turnSpeed * filter.output(), 1);
	}

	@Override
	protected boolean isFinished() {
		return pidAngle.onTarget();
	}

	@Override
	protected void end() {

		// Stop PID and the motors
		pidAngle.disable();
		Robot.drivetrain.drive(0, 0);
		
		Robot.drivetrain.reset();
		pidAngle.reset();
		
		System.out.println("TurnAngle ended");
	}
	
	protected void interrupted() {
		end();
	}
}
