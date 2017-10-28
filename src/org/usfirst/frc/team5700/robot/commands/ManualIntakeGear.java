package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5700.robot.Robot;

/**
 *
 */
public class ManualIntakeGear extends Command {

	double intakeSpeed, compressionSpeed;
	
    public ManualIntakeGear(double intakeSpeed, double compressionSpeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearIntake);
        this.intakeSpeed = intakeSpeed;
        this.compressionSpeed = compressionSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearIntake.gearIntakeDown();
    	Robot.gearIntake.intakeGear();;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearIntake.gearIntakeUp();
    	Robot.gearIntake.rollerHoldGear();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
