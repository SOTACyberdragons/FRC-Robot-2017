package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5700.robot.Robot;

/**
 *
 */
public class TankDriveWithJoysticksNoSq extends Command {

    public TankDriveWithJoysticksNoSq() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrainnosq);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrainnosq.tankDriveNoSq(Robot.oi.getLeftStick(), Robot.oi.getRightStick());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrainnosq.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
