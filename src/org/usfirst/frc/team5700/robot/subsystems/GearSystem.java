package org.usfirst.frc.team5700.robot.subsystems;


import org.usfirst.frc.team5700.robot.commands.GearDropReset;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

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

    public void initDefaultCommand() {
        setDefaultCommand(new GearDropReset());
    }
}

