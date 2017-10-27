package org.usfirst.frc.team5700.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team5700.robot.commands.AntiClimb;
import org.usfirst.frc.team5700.robot.commands.ClimbUp;
import org.usfirst.frc.team5700.robot.commands.DriveTowardsObjectWithVision;
import org.usfirst.frc.team5700.robot.commands.IntakeGear;
import org.usfirst.frc.team5700.robot.commands.ReverseDrive;
import org.usfirst.frc.team5700.robot.commands.SlowDrive;
import org.usfirst.frc.team5700.robot.commands.HangGear;
import org.usfirst.frc.team5700.robot.commands.IntakeDown;
//import org.usfirst.frc.team5700.robot.commands.DriveTowardsObjectWithVision;
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
	public OI() {
		////set buttons
		//drivetrain
		JoystickButton reverseDrive = new JoystickButton(rightStick, ButtonMap.REVERSE_DRIVE);
		JoystickButton slowDrive = new JoystickButton(rightStick, ButtonMap.SLOW_DRIVE);
		
		//gear intake
		JoystickButton intakeGear = new JoystickButton(rightStick, ButtonMap.INTAKE_GEAR);
		JoystickButton hangGear = new JoystickButton(leftStick, ButtonMap.HANG_GEAR);
		JoystickButton spitOut = new JoystickButton(leftStick, ButtonMap.SPIT_OUT);
		JoystickButton intakeDown = new JoystickButton(leftStick, ButtonMap.INTAKE_DOWN);
		//JoystickButton switchSafetyOff = new JoystickButton(leftStick, ButtonMap.SWITCH_SAFETY_OFF);
		
		//climber
		JoystickButton fastClimb = new JoystickButton(rightStick, ButtonMap.FAST_CLIMB);
		JoystickButton slowClimb = new JoystickButton(leftStick, ButtonMap.SLOW_CLIMB);
		JoystickButton anticlimb = new JoystickButton(rightStick, ButtonMap.ANTICLIMB);
		
		//vision test
		JoystickButton testVision = new JoystickButton(leftStick, 10);
	    
		////run commands
		//drivetrain
		//reverseDrive.whenActive(new ReverseDrive());
		//slowDrive.whenActive(new SlowDrive());
		
		//gear intake
		intakeGear.whileHeld(new IntakeGear(-0.6, -0.2));
		hangGear.whileHeld(new HangGear(0.7));
		spitOut.whileHeld(new SpitOut(0.7));
		intakeDown.whileHeld(new IntakeDown());
		//switchSafetyOff.whileHeld(new SwitchSafetyOff);
		
		//climber
		fastClimb.whileHeld(new ClimbUp(1));
		slowClimb.whileHeld(new ClimbUp(0.4));
		anticlimb.whileHeld(new AntiClimb());
		
		//test vision
		testVision.whileHeld(new DriveTowardsObjectWithVision());
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
}

