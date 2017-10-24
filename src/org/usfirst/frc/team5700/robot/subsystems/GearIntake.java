package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearIntake extends Subsystem {
    
	private DoubleSolenoid leftPiston, rightPiston;
	Spark intakeMotor;
	double intakeSpeed;
	double holdSpeed = 0.2;
    
    public GearIntake(double speed) {
    	super();
    	leftPiston = new DoubleSolenoid(0, 1);
    	leftPiston.set(DoubleSolenoid.Value.kForward);
    	
    	rightPiston = new DoubleSolenoid(2, 3);
    	rightPiston.set(DoubleSolenoid.Value.kForward);
    	
    	intakeMotor = new Spark(RobotMap.GEAR_INTAKE);
    	this.intakeSpeed = speed;
    }
	
	public void gearIntakeDown() {
		leftPiston.set(DoubleSolenoid.Value.kReverse);
		rightPiston.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void gearIntakeUp() {
		leftPiston.set(DoubleSolenoid.Value.kForward);
		rightPiston.set(DoubleSolenoid.Value.kForward);
	}
	
	public void setMotorSpeed(double speed) {
		intakeMotor.set(speed);
	}

	@Override
	protected void initDefaultCommand() {
	}
}

