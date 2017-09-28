package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;
import org.usfirst.frc.team5700.vision.BBLocator;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * 
 * @author romandimov
 *
 */

public class DriveTowardsObjectWithVision extends Command {
	
	private PIDController pidAngle;
	
	private double angleKp = 0.01;
	private double angleKi = 0.001;
	private double angleKd = 0;
	
	private double driveCurve = 0;
	private double driveOutput;
	
	private LinearAccelerationFilter filter;
	
	private double angleSetpoint = 0;

    public DriveTowardsObjectWithVision(double speed) {
       requires(Robot.drivetrain);
       
       driveOutput = speed;
       
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
       
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setAbsoluteTolerance(0.5);
		pidAngle.setSetpoint(angleSetpoint);
		
		LiveWindow.addActuator("Drive", "Angle controller", pidAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
		System.out.println("DriveStraight initialize");
		
		
    	Preferences prefs = Preferences.getInstance();
		double filterSlopeTime = prefs.getDouble("FilterSlopeTime", 0.5);
		filter = new LinearAccelerationFilter(filterSlopeTime);
	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//updates setpoint only if vision sees object
        if (BBLocator.seesObject()) {
        	angleSetpoint = BBLocator.getAngleFromHeading();
        }
        
    	Robot.drivetrain.drive(driveOutput * filter.output(), driveCurve);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Stop PID and the wheels

		pidAngle.disable();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();
		pidAngle.reset();
		
		System.out.println("DriveStraight ended");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
