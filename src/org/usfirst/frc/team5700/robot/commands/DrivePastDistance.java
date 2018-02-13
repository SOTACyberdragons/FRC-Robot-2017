package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.command.Command;

public class DrivePastDistance extends Command {

    private int direction = 1;
	private double distanceIn;
	private double speed;
	private boolean stop;
	private LinearAccelerationFilter filter;
	private boolean useRecordedDistance;

	public DrivePastDistance(double distance, double speed, boolean stop) {
        requires(Robot.drivetrain);
        
        this.distanceIn = distance;
        this.speed = speed;
        this.stop = stop;
    }
	
	public DrivePastDistance(double distance, double speed, boolean stop, boolean backwards) {
		this(distance, speed, stop);
		this.direction = backwards ? -1 : 1;
	}
	
	public DrivePastDistance(double speed, boolean stop) {
        requires(Robot.drivetrain);
        
        this.useRecordedDistance = true;
        this.speed = speed;
        this.stop = stop;
    }

    protected void initialize() {
    		//logs
    		System.out.println("Initializing DrivePastDistance Command");
    		
    		if (useRecordedDistance) {
    			
    			this.distanceIn = Robot.drivetrain.getLeftRecordedDistance();
    			this.distanceIn = Robot.drivetrain.getRightRecordedDistance();
    			System.out.println("Using recorded distance");
    			
    			
    		} else {
    			System.out.println("Using preset distance");
    		}
    		
    		System.out.println("First Distance: " + distanceIn * direction);
	    	System.out.println("driveSpeed: " + speed);
	    	
	    	Robot.drivetrain.reset();
	    	double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.2);
		filter = new LinearAccelerationFilter(filterSlopeTime);
    }

    protected void execute() {
    		Robot.drivetrain.drive(direction * speed * filter.output(), 0);
    }

    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.getLeftDistance()) > distanceIn;
    }

    protected void end() {
    		Robot.drivetrain.reset();
    		
    		if (stop) {
    			Robot.drivetrain.stop();
    		}
    		
    		System.out.println("DrivePastDistance Command Finished");
    }

    protected void interrupted() {
    		end();
    }
}
