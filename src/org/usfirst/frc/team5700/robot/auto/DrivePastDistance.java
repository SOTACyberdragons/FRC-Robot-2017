package org.usfirst.frc.team5700.robot.auto;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivePastDistance extends Command {

    private double distanceIn;
	private double speed;
	private LinearAccelerationFilter filter;

	public DrivePastDistance(double distance, double speed) {
        requires(Robot.drivetrain);
        
        this.distanceIn = distance;
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.2);
		filter = new LinearAccelerationFilter(filterSlopeTime);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.drive(speed * filter.output(), 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.drivetrain.getDistance() >= distanceIn;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
