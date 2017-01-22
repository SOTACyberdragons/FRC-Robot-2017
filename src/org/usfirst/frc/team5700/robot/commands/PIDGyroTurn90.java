package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PIDGyroTurn90 extends Command {
	
	private double turnAngle;

    public PIDGyroTurn90() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        
        //set angle for turn
        turnAngle = 90;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.PIDGyroTurn(turnAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.drivetrain.gyroOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
