package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ManualHangGear extends Command {
	
    public ManualHangGear() {
        requires(Robot.gearIntake);
    }

    protected void execute() {
    		Robot.gearIntake.gearIntakeDown();
    		Robot.gearIntake.rollerHangGear();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.gearIntake.gearIntakeUp();
    		Robot.gearIntake.stopRoller();
    }

    protected void interrupted() {
    		end();
    }
}