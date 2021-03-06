/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
public class AutoDriveStraightWithStop extends Command {
	private PIDController pidDistance;
	private PIDController pidAngle;
	private double driveOutput = 0;
	private double driveCurve = 0;

	private double distanceKp = 0.05;
	private double distanceKi = 0.005;
	private double distanceKd = 0;

	private double angleKp = 0.01;
	private double angleKi = 0.001;
	private double angleKd = 0;
	private double autoPower = 1.0;
	
	private LinearAccelerationFilter filter;


	public AutoDriveStraightWithStop(double distance) {
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
		pidDistance.setAbsoluteTolerance(0.5);
		pidDistance.setSetpoint(distance);
		
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setAbsoluteTolerance(0.5);
		pidAngle.setSetpoint(0.0);
		
		LiveWindow.addActuator("Drive", "Distance Controller", pidDistance);
		LiveWindow.addActuator("Drive", "Angle controller", pidAngle);
	}

	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidDistance.reset();
		pidAngle.reset();
		pidDistance.enable();
		pidAngle.enable();
		System.out.println("DriveStraightWithStop initialize");
		
		double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.5);
		autoPower = Robot.prefs.getDouble("Auto Power", 1.0);
				
		filter = new LinearAccelerationFilter(filterSlopeTime);
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutput * filter.output() * autoPower, driveCurve);
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
		Robot.drivetrain.drive(0, 0);
		
		Robot.drivetrain.reset();
		pidDistance.reset();
		pidAngle.reset();
		
		System.out.println("DriveStraightWithStop ended");
	}
}
