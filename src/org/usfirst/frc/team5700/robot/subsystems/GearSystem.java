package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.commands.GearDropReset;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

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
    	
    	gearDropSwitch = new DigitalInput(0);
    	
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
    
    public int gearSwitchCount() {
    	return gearSwitchCounter.get();
    }
    
    public void resetSwitchCount() {
    	gearSwitchCounter.reset();
    }
}

