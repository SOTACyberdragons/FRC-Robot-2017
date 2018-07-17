package org.usfirst.frc.team5700.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team5700.robot.commands.AntiClimb;
import org.usfirst.frc.team5700.robot.commands.ClimbUp;
import org.usfirst.frc.team5700.robot.commands.ManualIntakeGear;
import org.usfirst.frc.team5700.robot.commands.ManualHangGear;
import org.usfirst.frc.team5700.robot.commands.SpitOutGear;
import org.usfirst.frc.team5700.robot.commands.TurnOnLight;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private boolean toggle = false;
	private boolean hasBeenPressed = false;

	private XboxController controller = new XboxController(0);
	
	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;

	

	public OI() {
		////set buttons
		//drivetrain
		
		//set commands
		//gear intake
		controller.a.whileHeld(new ManualIntakeGear(-0.6, -0.3));
		controller.b.whileHeld(new ManualHangGear());
		controller.x.whileHeld(new SpitOutGear());

		//climber
		controller.y.whileHeld(new ClimbUp(1)); //fast climb
		controller.lb.whileHeld(new ClimbUp(0.4)); // slow climb
		controller.rb.whileHeld(new AntiClimb()); 

		//test vision
		controller.back.whileHeld(new TurnOnLight()); //light
	}

	public XboxController.Thumbstick getLeftStick() {
		return controller.leftStick;	
	}

	public XboxController.Thumbstick getRightStick() {
		return controller.rightStick;
	}

	public boolean getSquaredInput() {
		return squaredInput;
	}

	public Button slowClimb() {
		return controller.start;
	}

	public Button fastClimb() {
		return controller.dPad;
	}
}

