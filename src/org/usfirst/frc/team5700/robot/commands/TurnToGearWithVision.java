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
	private double angleKp;
	private double angleKi;
	private double angleKd;
	
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
		
		LiveWindow.addActuator("drivetrain", "Peg Vision Angle Controller", pidAngle);
    }

    protected void initialize() {
    
		//Get PID values from preferences
		Preferences prefs = Preferences.getInstance();
		angleKp = prefs.getDouble("TurnToGear Kp", 0.01);
		angleKi = prefs.getDouble("TurnToGear Ki", 0.001);
		angleKd = prefs.getDouble("TurnToGear Kd", 0.0);
		pidAngle.setAbsoluteTolerance(prefs.getDouble("TurnAngle Tol.", 4));

    		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();
		
		//logs
		System.out.println();
		System.out.println("TurnToGearWithVision Initialized");
	}

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

    protected boolean isFinished() {
        //switch off when peg pushes flap
    		return wasDetected && pidAngle.onTarget();
    }

    protected void end() {
	    	//record angle turned with vision
	    System.out.println("Angle Recorded; " + Robot.drivetrain.getHeading());
	    Robot.drivetrain.addToRecordedAngle();
	    	System.out.println("Total Angle Recorde: " + Robot.drivetrain.getRecordedAngle() + " Degrees");
	    	
	    	// Stop PID and the wheels
		pidAngle.disable();
		pidAngle.reset();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();
		
		System.out.println("TurnToGearWithVision ended");
    }

    protected void interrupted() {
    		end();
    }
}
