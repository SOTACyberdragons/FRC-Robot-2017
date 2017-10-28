package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;
import org.usfirst.frc.team5700.vision.BBoxLocator;
import org.usfirst.frc.team5700.vision.BBoxLocator.AngleDistance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author romandimov
 *
 */

public class DriveTowardsObjectWithVision extends Command {
	
	private PIDController pidAngle;
	
	private double angleKp = 0.002;
	private double angleKi = 0.00001;
	private double angleKd = 0.0007;
	
	private double driveCurve = 0;
	private double driveOutput;
	
	private LinearAccelerationFilter filter;
	
	private BBoxLocator bBoxLocator = new BBoxLocator();

    public DriveTowardsObjectWithVision() {
       requires(Robot.drivetrain);
       
       //Preferences prefs;
       //prefs = Preferences.getInstance();
       driveOutput = 0.8; //prefs.getDouble("Drive with Vision Speed", 0.8);
       
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
		//pidAngle.setAbsoluteTolerance(0.0);
		
		LiveWindow.addActuator("Drive", "Angle Controller", pidAngle);
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
        AngleDistance angle = bBoxLocator.getAngleFromHeading();

    	SmartDashboard.putNumber("PID Vision Setpoint Angle", pidAngle.getSetpoint());
    	
        if (angle != null) {
        	double m_angle = angle.angleDeg;
        	Robot.drivetrain.reset();
    		pidAngle.setSetpoint(m_angle);
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
