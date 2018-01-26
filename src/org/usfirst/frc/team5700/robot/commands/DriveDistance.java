package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class DriveDistance extends Command {
	
	private PIDController PIDController;
	private PIDSource pidSource;
	private PIDOutput pidOutput;
	
	private double driveOutput = 0;

	private double kP;
	private double kI;
	private double kD;

	public DriveDistance(double distance) {
		requires(Robot.drivetrain);
		
		pidSource = new PIDSource() {
			
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
		};
		
		pidOutput = new PIDOutput() {
			
			@Override
			public void pidWrite(double d) {
				driveOutput = d;
			}
		};
		
		
		PIDController = new PIDController(kP, kI, kD, pidSource, pidOutput);

		PIDController.setOutputRange(-1.0, 1.0);
		PIDController.setSetpoint(distance);
		
		LiveWindow.addActuator("Drive", "Distance Controller", PIDController);
	}
	
	@Override
	protected void initialize() {
		Preferences prefs = Preferences.getInstance();
		//get PID constants from Preferences Table
		kP = prefs.getDouble("kP", 0.01);
		kI = prefs.getDouble("kI", 0.0);
		kD = prefs.getDouble("kD", 0.0);

		PIDController.setAbsoluteTolerance(prefs.getDouble("Tol", 1));
		
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		PIDController.reset();
		PIDController.enable();
		
		System.out.println("DriveDistance initialized: kP:" + kP + " kI: " + kI + " kD: " + kD);
	}

	@Override
	protected void execute() {
		Robot.drivetrain.drive(driveOutput, 0);
	}

	@Override
	protected boolean isFinished() {
		return PIDController.onTarget();
	}

	@Override
	protected void end() {

		// Stop PID and the wheels
		PIDController.disable();
		Robot.drivetrain.stop();
		
		Robot.drivetrain.reset();
		PIDController.reset();
		
		System.out.println("DriveStraight ended");
	}
}
