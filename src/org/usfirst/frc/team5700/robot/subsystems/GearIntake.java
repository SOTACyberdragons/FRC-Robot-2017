package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearIntake extends Subsystem {  
    
	public Compressor compressor = new Compressor();
	
	private DoubleSolenoid leftPiston, rightPiston;
	private Solenoid ringLight;
	Spark rollerMotor;
	double intakeRollerSpeed = -0.8;
	double holdRollerSpeed = -0.3;
	double rollerHangSpeed = 0.4;
	
	DigitalInput pegSwitch;
    
    public GearIntake() {
		super();
	    	leftPiston = new DoubleSolenoid(0, 1);
	    	leftPiston.set(DoubleSolenoid.Value.kForward);
	    	
	    	rightPiston = new DoubleSolenoid(2, 3);
	    	rightPiston.set(DoubleSolenoid.Value.kForward);
	    	
	    	ringLight = new Solenoid(4);
	    	ringLight.set(false);
	    	
	    	rollerMotor = new Spark(RobotMap.GEAR_INTAKE);
	    	
	    pegSwitch = new DigitalInput(RobotMap.PEG_SWITCH);
    }
	public boolean getPegSwitch() {
		return pegSwitch.get();
	}
	
	public void gearIntakeDown() {
		leftPiston.set(DoubleSolenoid.Value.kReverse);
		rightPiston.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void gearIntakeUp() {
		leftPiston.set(DoubleSolenoid.Value.kForward);
		rightPiston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void setRollerSpeed(double speed) {
		rollerMotor.set(speed);
	}
	
	public void intakeGear() {
		rollerMotor.set(intakeRollerSpeed);
	}

	public void rollerHoldGear() {
		rollerMotor.set(holdRollerSpeed);
	}
	
	public void rollerHangGear() {
		rollerMotor.set(rollerHangSpeed);
	}

	public void stopRoller() {
		rollerMotor.set(0.0);
	}
	
	public void lightOn() {
		ringLight.set(true);
	}
	
	public void lightOff() {
		ringLight.set(false);
	}

	@Override
	protected void initDefaultCommand() {
	}
	
	public void log() {
	}
}

