package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Dimensions;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.vision.BBoxLocator;
import org.usfirst.frc.team5700.vision.BBoxLocator.BBox;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author romandimov
 *
 */

public class TurnToGearWithVision extends Command {
	
	private PIDController pidAngle;
	
	//TODO tune
	private double angleKp = 0.03;
	private double angleKi = 0.0001;
	private double angleKd = 0.001;
	
	private double driveOutput;
	
	private BBoxLocator bBoxLocator = new BBoxLocator(Dimensions.GEAR_WIDTH_IN);

    Preferences prefs = Preferences.getInstance();

	private boolean wasDetected = false;

	//TODO find spec
    private static final double MAX_WIDTH = 640;

    public TurnToGearWithVision() {
       requires(Robot.drivetrain);
       
       pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveOutput = c;
			}
		});
       
		pidAngle.setOutputRange(-1.0, 1.0);
		pidAngle.setAbsoluteTolerance(5); //Degrees
		
		LiveWindow.addActuator("drivetrain", "Peg Vision Angle Controller", pidAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    		System.out.println();
        	System.out.println("Initiate Turn to Gear with Vision");
    		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//updates setpoint only if vision sees object
        BBox bBox = bBoxLocator.getBBox();
    	
        if (bBox != null) {
            System.out.println("Can See Gear With Vision");
        		wasDetected = true;
	    		pidAngle.setSetpoint(Robot.drivetrain.getHeading() + bBox.angleDeg);
        		SmartDashboard.putNumber("PID Vision Setpoint Angle", pidAngle.getSetpoint());
        }
        //Turn in place
    		if (wasDetected) {
    			Robot.drivetrain.drive(driveOutput, 1);
    		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //switch off when peg pushes flap
    		return wasDetected && pidAngle.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
	    	//record driven distance
	    	System.out.println("Vision turn to gear recorded angle");
	    System.out.println("Vision Angle Recorded; " + Robot.drivetrain.getHeading());
	    Robot.drivetrain.addToRecordedAngle();
	    	System.out.println("Total Angle Recorded; " + Robot.drivetrain.getRecordedAngle());
	    	
	    	// Stop PID and the wheels
		pidAngle.disable();
		pidAngle.reset();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}