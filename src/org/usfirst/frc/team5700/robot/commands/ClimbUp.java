package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.AutoControls;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber.Direction;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbUp extends Command {

	private double speed;
	private boolean auto;

	public ClimbUp(double climbSpeed, boolean auto) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.ropeClimber);
		
		speed = climbSpeed;
		this.auto = auto;
	}
	
	public ClimbUp(double climbSpeed) {
		this(climbSpeed, false);
	}

	protected void initialize() {
		Robot.gearIntake.compressor.setClosedLoopControl(false);
	}

	protected void execute() {
		if (!auto || AutoControls.fastClimb())
			Robot.ropeClimber.climb(speed, Direction.UP);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.ropeClimber.climb(0);
		Robot.gearIntake.compressor.setClosedLoopControl(true);
	}

	protected void interrupted() {
		end();
	}
}
