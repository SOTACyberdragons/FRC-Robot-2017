package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class SpitOutGear extends Command {

	private double speed = 0.7;
	
    public SpitOutGear() {
        requires(Robot.gearIntake);
    }

    protected void execute() {
    		Robot.gearIntake.setRollerSpeed(speed);
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    		Robot.gearIntake.stopRoller();
    }

    protected void interrupted() {
    		end();
    }
}