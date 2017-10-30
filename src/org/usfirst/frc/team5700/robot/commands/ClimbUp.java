package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbUp extends Command {
	
	private double speed;

    public ClimbUp(double climbSpeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ropeClimber);
        
        speed = climbSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		Robot.gearIntake.compressor.setClosedLoopControl(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ropeClimber.climb(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    		Robot.ropeClimber.climb(0);
    		Robot.gearIntake.compressor.setClosedLoopControl(true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
