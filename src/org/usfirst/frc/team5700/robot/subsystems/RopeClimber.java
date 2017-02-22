package org.usfirst.frc.team5700.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5700.robot.RobotMap;

/**
 *
 */
public class RopeClimber extends Subsystem {

	private double speed;
	
	private SpeedController climb1;
	
	public RopeClimber() {
		climb1 = new Spark(RobotMap.CLIMB_MOTORS);
	}
	
	public void climb(double speed) {
		this.speed = speed;
		climb1.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void log() {
		SmartDashboard.putNumber("Speed", speed);
	}
}

