package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

	
public class ArcadeDriveWithJoysticks extends Command {
	private double power = 1.0;
	
	protected void initialize() {
		// Get everything in a safe starting state.
		
		
    	Preferences prefs = Preferences.getInstance();
		//double filterSlopeTime = prefs.getDouble("Filter Slope Time", 0.5);
		power = prefs.getDouble("Power", 1.0);
		System.out.println("Power: " + power);
				
		//filter = new LinearAccelerationFilter(filterSlopeTime);
	}

    public ArcadeDriveWithJoysticks() {
    	requires(Robot.drivetrain);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.arcadeDrive(Robot.oi.getLeftStick(), Robot.oi.getRightStick(),
    			Robot.oi.getSquaredInput(), power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.drive(0, 0);
    }
}
