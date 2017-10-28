package org.usfirst.frc.team5700.robot.auto;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HangGear extends Command {
	
	Timer timer = new Timer();

	double speed = 0.7;	
	
    public HangGear() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearIntake);
        timer.start();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearIntake.gearIntakeDown();
    	Robot.gearIntake.setMotorSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > 0.5;
    }

    // Called once after isFinished returns true
    protected void end() {
    		Robot.gearIntake.gearIntakeUp();
    		Robot.gearIntake.setMotorSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
