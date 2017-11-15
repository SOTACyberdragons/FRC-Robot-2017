package org.usfirst.frc.team5700.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5700.robot.RobotMap;

public class RopeClimber extends Subsystem {

	public enum Direction {UP, DOWN};
	private double speed;
	
	private Spark climber = new Spark(RobotMap.CLIMB_MOTORS);
	
	public void climb(double speed) {
		this.speed = -speed;
		climber.set(-speed);
	}
	
	public void climb(double speed, Direction direction) {
		this.speed = speed;
		int sign = (direction == Direction.UP) ? 1 : -1;
		
		climber.set(sign * speed);
	}
	
    public void initDefaultCommand() {
    }
    
	public void log() {
		SmartDashboard.putNumber("Speed", speed);
	}
}

