package org.usfirst.frc.team5700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team5700.robot.auto.PegWithVision;
import org.usfirst.frc.team5700.robot.commands.AntiClimb;
import org.usfirst.frc.team5700.robot.commands.ClimbUp;
import org.usfirst.frc.team5700.robot.commands.ManualIntakeGear;
import org.usfirst.frc.team5700.robot.commands.ManualHangGear;
import org.usfirst.frc.team5700.robot.commands.ManualIntakeDown;
import org.usfirst.frc.team5700.robot.commands.SpitOut;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

//random edit

public class OI {
	
	//create Joysticks and squared inputs for driving tankDrive

		private Joystick rightStick = new Joystick(0);
		private Joystick leftStick = new Joystick(1);
	    
	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;
	//// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
	
	JoystickButton reverseDrive;
	JoystickButton slowDrive;
	
	//gear intake
	JoystickButton intakeGear;
	JoystickButton hangGear;
	JoystickButton spitOut;
	JoystickButton intakeDown;
	JoystickButton PegButtonSafetyOff;
	//JoystickButton switchSafetyOff = new JoystickButton(leftStick, ButtonMap.SWITCH_SAFETY_OFF);
	
	//climber
	JoystickButton fastClimb;
	JoystickButton slowClimb;
	JoystickButton anticlimb;
	
	//vision test
	JoystickButton testVision;
	
	public OI() {
		////set buttons
		//drivetrain
		reverseDrive = new JoystickButton(rightStick, ButtonMap.REVERSE_DRIVE);
		slowDrive = new JoystickButton(rightStick, ButtonMap.SLOW_DRIVE);
		
		//gear intake
		intakeGear = new JoystickButton(rightStick, ButtonMap.INTAKE_GEAR);
		hangGear = new JoystickButton(leftStick, ButtonMap.HANG_GEAR);
		spitOut = new JoystickButton(leftStick, ButtonMap.SPIT_OUT);
		intakeDown = new JoystickButton(leftStick, ButtonMap.INTAKE_DOWN);
		PegButtonSafetyOff = new JoystickButton(leftStick, ButtonMap.PEG_BUTTON_SAFETY_OFF);
		//JoystickButton switchSafetyOff = new JoystickButton(leftStick, ButtonMap.SWITCH_SAFETY_OFF);
		
		//climber
		fastClimb = new JoystickButton(rightStick, ButtonMap.FAST_CLIMB);
		slowClimb = new JoystickButton(rightStick, ButtonMap.SLOW_CLIMB);
		anticlimb = new JoystickButton(rightStick, ButtonMap.ANTICLIMB);
		
		//vision test
		testVision = new JoystickButton(leftStick, 10);
	    
		////run commands
		//drivetrain
		//reverseDrive.whenActive(new ReverseDrive());
		//slowDrive.whenActive(new SlowDrive());
		
		//gear intake
		intakeGear.whileHeld(new ManualIntakeGear(-0.6, -0.3));
		hangGear.whileHeld(new ManualHangGear(0.7));
		spitOut.whileHeld(new SpitOut(0.7));
		intakeDown.whileHeld(new ManualIntakeDown());
		//switchSafetyOff.whileHeld(new SwitchSafetyOff);
		
		//climber
		fastClimb.whileHeld(new ClimbUp(1));
		slowClimb.whileHeld(new ClimbUp(0.4));
		anticlimb.whileHeld(new AntiClimb());
		
		//test vision
		testVision.whileHeld(new PegWithVision(true));
	}
	    
	public Joystick getLeftStick() {
	    	return leftStick;	
	    }
	    
	public Joystick getRightStick() {
	    	return rightStick;
	}
	    
	public boolean getSquaredInput() {
	    	return squaredInput;
	}

	public boolean isPegButtonSafetyOff() {
		return PegButtonSafetyOff.get();
	}
}

