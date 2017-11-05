package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TurnOnLight extends Command {

    public TurnOnLight() {
    }

    protected void initialize() {
    		Robot.gearIntake.lightOn();
    }
    
    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.gearIntake.lightOff();
    }

    protected void interrupted() {
    		end();
    }
}
