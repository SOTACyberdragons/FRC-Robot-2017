
package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
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
	private double distanceInches;

	private double kP;
	private double kI;
	private double kD;
	
	private double setpoint;
	private double endpoint;
	private double startpoint;
	private double time;
	private double velocity;
	
	private Timer timer;
	
	public DriveDistance() {
		requires(Robot.drivetrain);
		
		this.distanceInches = distanceInches;
	}
	
	
	@Override
	protected void initialize() {
		timer = new Timer();
		timer.start();
		
		Preferences prefs = Preferences.getInstance();
		//get PID constants from Preferences Table
		kP = prefs.getDouble("kP", 0.01);
		kI = prefs.getDouble("kI", 0.0);
		kD = prefs.getDouble("kD", 0.0);
		
		endpoint = prefs.getDouble("Endpoint", 0);
		setpoint = prefs.getDouble("Setpoint", 0);
		velocity = prefs.getDouble("Velocity", 1);
		startpoint = Robot.drivetrain.getDistance();
		
				
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
				Robot.drivetrain.drive(driveOutput, 0);
			}
		};
		
		
		PIDController = new PIDController(kP, kI, kD, pidSource, pidOutput);

		PIDController.setOutputRange(-1.0, 1.0);
		PIDController.setSetpoint(setpoint);
		
		LiveWindow.addActuator("Drive", "Distance Controller", PIDController);

		PIDController.setAbsoluteTolerance(prefs.getDouble("Tol", 0.25));
		
		// Get everything in a safe starting state.
		Robot.drivetrain.reset();
		PIDController.reset();
		PIDController.enable();
		
		System.out.println("DriveDistance initialized: kP:" + kP + " kI: " + kI + " kD: " + kD);
		System.out.println("Current Setpoint: " + setpoint);
	}

	@Override
	protected void execute() {
		//System.out.println("Feedback position: " + Robot.drivetrain.getDistance());
		//System.out.println("Drive Output: " + driveOutput);
		time =+ .05;
		setpoint = startpoint + time * velocity;
		PIDController.setSetpoint(setpoint);
		System.out.println(setpoint);
		System.out.println(time);
	}

	@Override
	protected boolean isFinished() {
		//return timer.get() > 60;
		return (Math.abs(endpoint - setpoint) < 0.5);
		
		
	}

	@Override
	protected void end() { 

		// Stop PID and the wheels
		PIDController.disable();
		Robot.drivetrain.stop();
		PIDController.reset();
		
		System.out.println(setpoint);
		System.out.println("DriveDistance ended");
	}
	
	protected void interrupted() {
		end();
	}
	public void Log() {
		System.out.print("Finished PID command");
	}
}
