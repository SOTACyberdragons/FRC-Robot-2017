package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SpinIntakeBackwardLeftJoystick extends Command {
	

    public SpinIntakeBackwardLeftJoystick() {
    		requires(Robot.gearIntake);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = Robot.oi.getRightStick().getX();
<<<<<<< HEAD
    	double positiveSpeed = Math.min(0,speed);
    	Robot.gearIntake.setRollerSpeed(positiveSpeed);
=======
    	double negitaveSpeed = Math.min(0,speed);
    	Robot.gearIntake.setRollerSpeed(negitaveSpeed);
>>>>>>> b1f8b5c89a9586656548137147907eb02927690b
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearIntake.stopRoller();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
    
}
