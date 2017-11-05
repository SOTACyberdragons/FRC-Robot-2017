package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5700.robot.Robot;

public class ManualIntakeGear extends Command {
	
    public ManualIntakeGear() {
        requires(Robot.gearIntake);
    }

    protected void execute() {
    		Robot.gearIntake.gearIntakeDown();
    		Robot.gearIntake.intakeGear();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.gearIntake.gearIntakeUp();
    		Robot.gearIntake.rollerHoldGear();
    }

    protected void interrupted() {
    		end();
    }
}