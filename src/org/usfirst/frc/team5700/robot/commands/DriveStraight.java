/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
public class DriveStraight extends Command {
	private PIDController pidDistance;
	private PIDController pidAngle;
	private double driveOutput = 0;
	private double driveCurve = 0;

	private double distanceKp;
	private double distanceKi;
	private double distanceKd;

	private double angleKp;
	private double angleKi;
	private double angleKd;
	
	private LinearAccelerationFilter filter;

	public DriveStraight(double distance) {
		requires(Robot.drivetrain);
		pidDistance = new PIDController(distanceKp, 
				distanceKi, 
				distanceKd, 
				new PIDSource() {
			PIDSourceType m_sourceType = PIDSourceType.kDisplacement;

			@Override
			public double pidGet() {
				return Robot.drivetrain.getDistance();
			}

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				m_sourceType = pidSource;
			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return m_sourceType;
			}
		}, 
				new PIDOutput() {
			@Override
			public void pidWrite(double d) {
				driveOutput = d;
			}
		});
		pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveCurve = c;
			}
		});

		pidDistance.setOutputRange(-1.0, 1.0);
		pidDistance.setSetpoint(distance);
		
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setSetpoint(0);
		
		LiveWindow.addActuator("Drive", "Distance Controller", pidDistance);
		LiveWindow.addActuator("Drive", "Angle controller", pidAngle);
	}
	
	@Override
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();
		//get PID constants from Preferences Table
		distanceKp = prefs.getDouble("DriveStraight D Kp", 0.05);
		distanceKi = prefs.getDouble("DriveStraight D Ki", 0.005);
		distanceKd = prefs.getDouble("DriveStraight D Kd", 0.0);

		angleKp = prefs.getDouble("DriveStraight A Kp", 0.01);
		angleKi = prefs.getDouble("DriveStraight A Ki", 0.001);
		angleKd = prefs.getDouble("DriveStraight A Kd", 0.0);

		pidDistance.setAbsoluteTolerance(prefs.getDouble("DriveStraight D Tol.", 3));
		pidAngle.setAbsoluteTolerance(prefs.getDouble("DriveStraight A Tol.", 0.5));
		
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidDistance.reset();
		pidAngle.reset();
		pidDistance.enable();
		pidAngle.enable();
		
		double filterSlopeTime = prefs.getDouble("FilterSlopeTime", 0.5);
		filter = new LinearAccelerationFilter(filterSlopeTime);
		
		System.out.println("DriveStraight initialized");
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutput * filter.output(), driveCurve);
	}

	@Override
	protected boolean isFinished() {
		return pidDistance.onTarget();
	}

	@Override
	protected void end() {

		// Stop PID and the wheels
		pidDistance.disable();
		pidAngle.disable();
		Robot.drivetrain.stop();
		
		Robot.drivetrain.reset();
		pidDistance.reset();
		pidAngle.reset();
		
		System.out.println("DriveStraight ended");
	}
}
