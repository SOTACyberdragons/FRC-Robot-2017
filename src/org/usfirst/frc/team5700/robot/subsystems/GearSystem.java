package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearSystem extends Subsystem {
    
	private DoubleSolenoid gearDropPiston;
    
    public GearSystem() {
    	super();
    	gearDropPiston = new DoubleSolenoid(0, 1);
    	gearDropPiston.set(DoubleSolenoid.Value.kReverse);
    }
	
	public void gearHolderDown() {
		gearDropPiston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void gearHolderUp() {
		gearDropPiston.set(DoubleSolenoid.Value.kReverse);
	}

	@Override
	protected void initDefaultCommand() {
	}
}

