package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearDropAutomatic extends Command {

    public GearDropAutomatic() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearsystem.gearHolderDown();
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
    	Robot.gearsystem.gearHolderUp();
    	Robot.wasPressed = false;
    	Robot.gearsystem.resetSwitchCount();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
