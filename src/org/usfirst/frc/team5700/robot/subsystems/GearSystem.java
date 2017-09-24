package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.GearDropReset;

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
	private DigitalInput gearDropSwitch;
	private Counter gearSwitchCounter;
    
    public GearSystem() {
    	super();
    	gearDropPiston = new DoubleSolenoid(0, 1);
    	gearDropPiston.set(DoubleSolenoid.Value.kReverse);
    	
    	gearDropSwitch = new DigitalInput(RobotMap.GEAR_TRIGGER);
    	
    	gearSwitchCounter = new Counter(gearDropSwitch);
    }
	
	public void gearHolderDown() {
		gearDropPiston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void gearHolderUp() {
		gearDropPiston.set(DoubleSolenoid.Value.kReverse);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new GearDropReset());
    }
    
    public DigitalInput gearDropSwitch() {
    	return gearDropSwitch;
    }
    
    public boolean dropSwitchTriggered() {
    	return gearDropSwitch.get();
    }
    
    public boolean gearSwitchPushed() {
    	return gearSwitchCounter.get() > 0;
    }
    
    public void resetSwitchCount() {
    	gearSwitchCounter.reset();
    }
    
    public Counter getGearSwitchCounter() {
    	return gearSwitchCounter;
    }
    
	public void log() {
		SmartDashboard.putNumber("Counter", gearSwitchCounter.get());
	}
}

