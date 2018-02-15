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
	
	private double pidLeftOutputVal;
	private double pidRightOutputVal;
	
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
		leftEndpoint = prefs.getDouble("LeftEndpoint", 0);
		leftVelocity = prefs.getDouble("LeftVelocity", 1);
		leftStartpoint = Robot.drivetrain.getLeftDistance();
		leftSetpoint = leftStartpoint;
		
		rightEndpoint = prefs.getDouble("RightEndpoint", 0);
		rightVelocity = prefs.getDouble("RightVelocity", 1);
		rightStartpoint = Robot.drivetrain.getRightDistance();
		rightSetpoint = rightStartpoint;
		
		pidLeftOutputVal = 0;
		pidRightOutputVal = 0;
		
		if (leftEndpoint < leftStartpoint)
			leftVelocity = -leftVelocity;
			
		if (rightEndpoint < rightStartpoint)
			rightVelocity = -rightVelocity;
		
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
				pidLeftOutputVal = d;
				Robot.drivetrain.runLeftMotors(pidLeftOutputVal);
			}
		};
		
		pidSourceRight = new PIDSource() {
			
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
		};
		
		pidOutputRight = new PIDOutput() {
			
			@Override
			public void pidWrite(double d) {
				pidRightOutputVal = d;
				Robot.drivetrain.runRightMotors(pidRightOutputVal);
			}
		};
		
		PIDControllerLeft = new PIDController(kP, kI, kD, pidSourceLeft, pidOutputLeft);
		PIDControllerRight = new PIDController(kP, kI, kD, pidSourceRight, pidOutputRight);

		PIDControllerLeft.setOutputRange(-1.0, 1.0);
		PIDControllerLeft.setSetpoint(leftSetpoint);
		PIDControllerRight.setOutputRange(-1.0, 1.0);
		PIDControllerRight.setSetpoint(rightSetpoint);
		
		LiveWindow.addActuator("Drive", "Distance Controller", PIDControllerLeft);

		PIDControllerLeft.setAbsoluteTolerance(prefs.getDouble("Tol", 0.25));
		PIDControllerRight.setAbsoluteTolerance(prefs.getDouble("Tol", 0.25));
		
		// Get everything in a safe starting state.
		PIDControllerLeft.reset();
		PIDControllerLeft.enable();
		PIDControllerRight.reset();
		PIDControllerRight.enable();
		
		System.out.println("DriveDistance initialized: kP:" + kP + " kI: " + kI + " kD: " + kD);
		System.out.println("Current leftSetpoint: " + leftSetpoint);
		System.out.println("Current rightSetpoint: " + rightSetpoint);
	}

	@Override
	protected void execute() {
		time += .02;
		leftSetpoint = leftStartpoint + time * leftVelocity;
		rightSetpoint = rightStartpoint + time * rightVelocity;
		PIDControllerLeft.setSetpoint(leftSetpoint);
		PIDControllerRight.setSetpoint(rightSetpoint);
		System.out.println(leftSetpoint);
		System.out.println(rightSetpoint);
		System.out.println(time);
	}

	@Override
	protected boolean isFinished() {
		return ((Math.abs(leftEndpoint - leftSetpoint) < 0.5) && (Math.abs(rightEndpoint - rightSetpoint) < 0.5));
		
	}
	
	@Override
	protected void end() { 
		PIDControllerLeft.disable();
		PIDControllerRight.disable();
		Robot.drivetrain.stop();
		PIDControllerLeft.reset();
		PIDControllerRight.reset();
		System.out.println("DriveDistance ended");
	}
	
	protected void interrupted() {
		end();
	}
	
	public void Log() {
		System.out.print("Finished PID command");
	}
}
