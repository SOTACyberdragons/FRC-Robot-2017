package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearDropOnly extends Command {

    public GearDropOnly() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("GearDropOnly initialize");
    	Robot.gearSystem.gearHolderDown();
    	SmartDashboard.putBoolean("Gear Placed", true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return timeSinceInitialized() > 1.5;
    }

    // Called once after isFinished returns true
    protected void end() {

    	System.out.println("GearDropOnly ended");
    	SmartDashboard.putBoolean("Gear Down", true);
    
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}