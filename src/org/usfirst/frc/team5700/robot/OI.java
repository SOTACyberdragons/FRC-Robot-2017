package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.TurnNinetyClockwise;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

public class OI {
	
	//create Joysticks and squared inputs for driving tankDrive
		private Joystick rightStick = new Joystick(1);
		private Joystick leftStick = new Joystick(0);
	    
	    // Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	    private boolean squaredInput = true;
	    
	    public OI() {
			//// CREATING BUTTONS
		    // One type of button is a joystick button which is any button on a joystick.
		    // You create one by telling it which joystick it's on and which button
		    // number it is.
		    // Joystick stick = new Joystick(port);
		    JoystickButton leftTrigger = new JoystickButton(leftStick, 1);
		    
		    // There are a few additional built in buttons you can use. Additionally,
		    // by subclassing Button you can create custom triggers and bind those to
		    // commands the same as any other Button.
		    
		    //// TRIGGERING COMMANDS WITH BUTTONS
		    // Once you have a button, it's trivial to bind it to a button in one of
		    // three ways:
		    
		    // Start the command when the button is pressed and let it run the command
		    // until it is finished as determined by it's isFinished method.
		    // button.whenPressed(new ExampleCommand());
		    leftTrigger.whileHeld(new TurnNinetyClockwise());
		    
		    // Run the command while the button is being held down and interrupt it once
		    // the button is released.
		    // button.whileHeld(new ExampleCommand());
		    
		    // Start the command when the button is released  and let it run the command
		    // until it is finished as determined by it's isFinished method.
		    // button.whenReleased(new ExampleCommand());
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

