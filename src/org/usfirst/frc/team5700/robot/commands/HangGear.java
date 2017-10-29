package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HangGear extends Command {
	
	Timer timer = new Timer();

	double gearIntakeDownTime = 0.5;
	
    public HangGear() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearIntake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearIntake.gearIntakeDown();
    	Robot.gearIntake.rollerHangGear();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > gearIntakeDownTime;
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.gearIntake.gearIntakeUp();
		Robot.gearIntake.stopRoller();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
