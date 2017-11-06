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

	/**
	 * @param distance
	 * @param speed
	 * @param stop?
	 */
	public DrivePastDistance(double distance, double speed, boolean stop) {
        requires(Robot.drivetrain);
        
        this.distanceIn = distance;
        this.speed = speed;
        this.stop = stop;
    }
	
	/**
	 * @param distance
	 * @param speed
	 * @param stop?
	 * @param backwards?
	 */
	public DrivePastDistance(double distance, double speed, boolean stop, boolean backwards) {
		this(distance, speed, stop);
		this.direction = backwards ? -1 : 1;
	}
	
	/**
	 * This constructor will make the command use the recorded distance
	 * @param speed
	 * @param stop?
	 */
	public DrivePastDistance(double speed, boolean stop) {
        requires(Robot.drivetrain);
        
        this.useRecordedDistance = true;
        this.speed = speed;
        this.stop = stop;
    }

    protected void initialize() {
		System.out.println("\nInitializing DrivePastDistance...");
		
    		if (useRecordedDistance) {
    			this.distanceIn = Robot.drivetrain.getRecordedDistance();
    			System.out.println("Using recorded distance");
    		} else {
    			System.out.println("Using preset distance");
    		}
    		
    		System.out.println("distance: " + distanceIn * direction);
	    	System.out.println("driveSpeed: " + speed);
	    	
	    	Robot.drivetrain.reset();
	    	double filterSlopeTime = Robot.prefs.getDouble("FilterSlopeTime", 0.2);
		filter = new LinearAccelerationFilter(filterSlopeTime);
		
		System.out.println("DrivePastDistance Initialized");
    }

    protected void execute() {
    		Robot.drivetrain.drive(direction * speed * filter.output(), 0);
    }

    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.getDistance()) > distanceIn;
    }

    protected void end() {
    		Robot.drivetrain.reset();
    		
    		if (stop) {
    			Robot.drivetrain.stop();
    		}
    		
    		System.out.println("DrivePastDistance Command ended");
    }

    protected void interrupted() {
    		end();
    }
}
