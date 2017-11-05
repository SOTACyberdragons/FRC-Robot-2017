package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber.Direction;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbUp extends Command {
	
	private double speed;

    public ClimbUp(double climbSpeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.ropeClimber);
        
        speed = climbSpeed;
    }

    protected void initialize() {
    		Robot.gearIntake.compressor.setClosedLoopControl(false);
    }

    protected void execute() {
    	Robot.ropeClimber.climb(speed, Direction.UP);
    }

    protected boolean isFinished() {
    	return false;
    }

    protected void end() {
    		Robot.ropeClimber.climb(0);
    		Robot.gearIntake.compressor.setClosedLoopControl(true);
    }

    protected void interrupted() {
    		end();
    }
}
