package org.usfirst.frc.team5700.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team5700.robot.RobotMap;

/**
 *
 */
public class RopeClimber extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private SpeedController climb1, climb2;
	
	public RopeClimber() {
		climb1 = new Spark(RobotMap.CLIMB1_MOTOR);
		climb2 = new Spark(RobotMap.CLIMB2_MOTOR);
	}
	
	public void climb(double speed) {
		climb1.set(speed);
		climb2.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

