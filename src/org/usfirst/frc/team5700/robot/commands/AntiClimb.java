package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber.Direction;

import edu.wpi.first.wpilibj.command.Command;

public class AntiClimb extends Command {

    public AntiClimb() {
        requires(Robot.ropeClimber);
    }

    protected void execute() {
    		Robot.ropeClimber.climb(0.5, Direction.DOWN);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.ropeClimber.climb(0);
    }

    protected void interrupted() {
    		end();
    }
}