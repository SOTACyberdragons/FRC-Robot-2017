package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AutoHangGear extends Command {
	
	Timer timer = new Timer();

	double gearIntakeDownTime = 0.5;
	
    public AutoHangGear() {
        requires(Robot.gearIntake);
    }

    protected void initialize() {
    	timer.start();
    }

    protected void execute() {
    	Robot.gearIntake.gearIntakeDown();
    	Robot.gearIntake.rollerHangGear();
    }

    protected boolean isFinished() {
        return timer.get() > gearIntakeDownTime;
    }

    protected void end() {
		Robot.gearIntake.gearIntakeUp();
		Robot.gearIntake.stopRoller();
    }

    protected void interrupted() {
    	end();
    }
}
