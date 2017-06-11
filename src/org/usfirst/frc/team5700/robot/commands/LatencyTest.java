package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LatencyTest extends Command {

    public LatencyTest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println(timeSinceInitialized());
    	Robot.cameraserver.startAutomaticCapture();
    	System.out.println(timeSinceInitialized());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.networkTableSensor.pidGet() > 0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println(timeSinceInitialized());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
