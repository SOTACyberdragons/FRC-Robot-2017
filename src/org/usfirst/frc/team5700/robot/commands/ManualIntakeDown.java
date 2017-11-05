package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ManualIntakeDown extends Command {

    public ManualIntakeDown() {
        requires(Robot.gearIntake);
    }

    protected void execute() {
    	Robot.gearIntake.gearIntakeDown();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.gearIntake.gearIntakeUp();
    }

    protected void interrupted() {
    		end();
    }
}
