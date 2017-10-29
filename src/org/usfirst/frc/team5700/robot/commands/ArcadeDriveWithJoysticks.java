package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArcadeDriveWithJoysticks extends Command {
	
	public ArcadeDriveWithJoysticks() {
    	requires(Robot.drivetrain);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.drivetrain.arcadeDrive(Robot.oi.getLeftStick(), Robot.oi.getRightStick(),
    			Robot.oi.getSquaredInput());
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
