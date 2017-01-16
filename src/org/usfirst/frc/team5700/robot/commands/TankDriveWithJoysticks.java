package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5700.robot.Robot;

/**
 *
 */
public class TankDriveWithJoysticks extends Command {

    public TankDriveWithJoysticks() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.tankDrive(Robot.oi.getLeftStick(), Robot.oi.getRightStick(),Robot.oi.getSquaredInput());
    	Robot.drivetrain.tankDriveNoSq(Robot.oi.getLeftStick(), Robot.oi.getRightStick());
    	Robot.drivetrain.arcadeDriveNoSq(Robot.oi.getLeftStick());
    	Robot.drivetrain.arcadeDrive(Robot.oi.getLeftStick(), Robot.oi.getSquaredInput());
    	Robot.drivetrain.planeDrive(Robot.oi.getLeftStick(), Robot.oi.getRightStick(), Robot.oi.getSquaredInput());
    	Robot.drivetrain.planeDriveNoSq(Robot.oi.getLeftStick(), Robot.oi.getRightStick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
