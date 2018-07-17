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
	private PIDController pidDistanceLeft;
	private PIDController pidDistanceRight;
	private PIDController pidAngleLeft;
	private PIDController pidAngleRight;
	private double driveOutputLeft;
	private double driveOutputRight;
	private double driveCurveLeft;
	private double driveCurveRight;

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
		pidDistanceLeft = new PIDController(distanceKp, 
				distanceKi, 
				distanceKd, 
				new PIDSource() {
			PIDSourceType m_sourceType = PIDSourceType.kDisplacement;

			@Override
			public double pidGet() {
				return Robot.drivetrain.getLeftDistance();
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
				driveOutputLeft = d;
			}
		});
		pidAngleLeft = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveCurveLeft = c;
			}
		});
		
		pidDistanceRight = new PIDController(distanceKp, 
				distanceKi, 
				distanceKd, 
				new PIDSource() {
			PIDSourceType m_sourceType = PIDSourceType.kDisplacement;

			@Override
			public double pidGet() {
				return Robot.drivetrain.getRightDistance();
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
				driveOutputLeft = d;
			}
		});
		pidAngleRight = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveCurveRight = c;
			}
		});
		
		pidDistanceLeft.setOutputRange(-1.0, 1.0);
		pidDistanceLeft.setAbsoluteTolerance(0.5);
		pidDistanceLeft.setSetpoint(distance);
		
		pidAngleLeft.setOutputRange(-1.0, 1.0);
		pidAngleLeft.setAbsoluteTolerance(0.5);
		pidAngleLeft.setSetpoint(0.0);
		
		LiveWindow.addActuator("Left Drive", "Left Distance Controller", pidDistanceLeft);
		LiveWindow.addActuator("Left Drive", "Left Angle controller", pidAngleLeft);
		
		pidDistanceRight.setOutputRange(-1.0, 1.0);
		pidDistanceRight.setAbsoluteTolerance(0.5);
		pidDistanceRight.setSetpoint(distance);
		
		pidAngleRight.setOutputRange(-1.0, 1.0);
		pidAngleRight.setAbsoluteTolerance(0.5);
		pidAngleRight.setSetpoint(0.0);
		
		LiveWindow.addActuator("Right Drive", "Right Distance Controller", pidDistanceRight);
		LiveWindow.addActuator("Right Drive", "Right Angle controller", pidAngleRight);
	}

	@Override
	protected void initialize() {
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		pidDistanceLeft.reset();
		pidDistanceRight.reset();
		pidAngleLeft.reset();
		pidAngleRight.reset();
		pidDistanceLeft.enable();
		pidDistanceRight.enable();
		pidAngleLeft.enable();
		pidAngleRight.enable();
		System.out.println("DriveStraightWithStop initialize");
		
		double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.5);
		autoPower = Robot.prefs.getDouble("Auto Power", 1.0);
				
		filter = new LinearAccelerationFilter(filterSlopeTime);
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutputLeft * filter.output() * autoPower, driveCurveLeft);
		Robot.drivetrain.drive(driveOutputRight * filter.output() * autoPower, driveCurveRight);
	}

	@Override
	protected boolean isFinished() {
		return pidDistanceLeft.onTarget();
	}

	@Override
	protected void end() {

		// Stop PID and the wheels
		pidDistanceLeft.disable();
		pidDistanceRight.disable();
		pidAngleLeft.disable();
		Robot.drivetrain.drive(0, 0);
		
		Robot.drivetrain.reset();
		pidDistanceLeft.reset();
		pidDistanceRight.reset();
		pidAngleLeft.reset();
		pidAngleRight.reset();
		
		System.out.println("DriveStraightWithStop ended");
	}
}
