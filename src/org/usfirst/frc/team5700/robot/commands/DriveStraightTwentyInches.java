package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class DriveStraightTwentyInches extends Command {

	
		private boolean stop;
		
		
    public DriveStraightTwentyInches() {
    	requires(Robot.drivetrain);
  
         
    	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.reset();
    	System.out.println("Initializing DrivePastDistance Command");
    	
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double distance = Robot.drivetrain.getDistance();
    	while(distance < 20) {
    		Robot.drivetrain.drive(0.5, 0);
    	}
    	
 
    		
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.reset();
		
		System.out.println("DriveStraightTwentyInches Command Finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
