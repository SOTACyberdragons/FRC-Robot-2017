
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
	
	private PIDController PIDControllerLeft;
	private PIDController PIDControllerRight;
	private PIDSource pidSourceLeft;
	private PIDSource pidSourceRight;
	private PIDOutput pidOutputLeft;
	private PIDOutput pidOutputRight;

	
	private double distanceInches;

	private double kP;
	private double kI;
	private double kD;
	
	private double leftSetpoint;
	private double rightSetpoint;
	private double leftEndpoint;
	private double rightEndpoint;
	private double leftStartpoint;
	private double rightStartpoint;
	private double time;
	private double leftVelocity;
	private double rightVelocity;
	
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
		kP = prefs.getDouble("kP", 0.2);
		kI = prefs.getDouble("kI", 0.01);
		kD = prefs.getDouble("kD", 0.0);
<<<<<<< HEAD
		leftEndpoint = prefs.getDouble("LeftEndpoint", 0);
		leftVelocity = prefs.getDouble("LeftVelocity", 1);
		leftStartpoint = Robot.drivetrain.getLeftDistance();
		leftSetpoint = leftStartpoint;
		
		if (leftEndpoint < leftStartpoint)
			leftVelocity = -leftVelocity;
		
		time = 0;
=======
		
		endpoint = prefs.getDouble("Endpoint", 0);
		setpoint = prefs.getDouble("Setpoint", 0);
		velocity = prefs.getDouble("Velocity", 1);
		startpoint = Robot.drivetrain.getDistance();
>>>>>>> 75bcee0807f97f085e365646ba1b2b79c80b85ac
		
				
		pidSourceLeft = new PIDSource() {
			
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
		};
		
		pidOutputLeft = new PIDOutput() {
			
			@Override
			public void pidWrite(double d) {
				Robot.drivetrain.tankDrive(0, d);
			}
		};
		
		
		PIDControllerLeft = new PIDController(kP, kI, kD, pidSourceLeft, pidOutputLeft);

		PIDControllerLeft.setOutputRange(-1.0, 1.0);
		PIDControllerLeft.setSetpoint(leftSetpoint);
		
		LiveWindow.addActuator("Drive", "Distance Controller", PIDControllerLeft);

		PIDControllerLeft.setAbsoluteTolerance(prefs.getDouble("Tol", 0.25));
		
		// Get everything in a safe starting state.
		//Robot.drivetrain.reset();
		PIDControllerLeft.reset();
		PIDControllerLeft.enable();
		
		System.out.println("DriveDistance initialized: kP:" + kP + " kI: " + kI + " kD: " + kD);
		System.out.println("Current Setpoint" + leftSetpoint);
		System.out.println("Current Setpoint: " + setpoint);
	}

	@Override
	protected void execute() {
		time += .02;
		leftSetpoint = leftStartpoint + time * leftVelocity;
		PIDControllerLeft.setSetpoint(leftSetpoint);
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
		PIDControllerLeft.disable();
		Robot.drivetrain.stop();
		PIDControllerLeft.reset();
		System.out.println(leftSetpoint);
		
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
