package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearHolderUp extends Command {

    public GearHolderUp() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("GearHolderUp initialize");
    	Robot.gearSystem.gearHolderUp();
    	SmartDashboard.putBoolean("Gear Placed", false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end(){
    	Robot.gearSystem.resetSwitchCount();
    	Robot.wasPressed = false;
    	System.out.println("GearHolderUp ended");
    	SmartDashboard.putBoolean("Gear Down", false);
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
