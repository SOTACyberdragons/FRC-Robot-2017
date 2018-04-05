package org.usfirst.frc.team5700.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
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

	private Joystick rightStick = new Joystick(0);
	private Joystick leftStick = new Joystick(1);

	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;

	JoystickButton slowDrive;
	JoystickButton toggleDirection;

	//gear intake
	JoystickButton intakeGear;
	JoystickButton hangGear;
	JoystickButton spitOut;

	//climber
	JoystickButton fastClimb;
	JoystickButton slowClimb;
	JoystickButton anticlimb;

	JoystickButton light;

	public OI() {
		////set buttons
		//drivetrain
		slowDrive = new JoystickButton(rightStick, ButtonMap.SLOW_DRIVE);
		toggleDirection = new JoystickButton(rightStick, ButtonMap.TOGGLE_DIRECTION);

		//gear intake
		intakeGear = new JoystickButton(rightStick, ButtonMap.INTAKE_GEAR);
		hangGear = new JoystickButton(leftStick, ButtonMap.HANG_GEAR);
		spitOut = new JoystickButton(leftStick, ButtonMap.SPIT_OUT);

		//climber
		fastClimb = new JoystickButton(leftStick, ButtonMap.FAST_CLIMB);
		slowClimb = new JoystickButton(leftStick, ButtonMap.SLOW_CLIMB);
		anticlimb = new JoystickButton(leftStick, ButtonMap.ANTICLIMB);

		light = new JoystickButton(rightStick, 10);

		//set commands
		//gear intake
		intakeGear.whileHeld(new ManualIntakeGear(-0.6, -0.3));
		hangGear.whileHeld(new ManualHangGear());
		spitOut.whileHeld(new SpitOutGear());

		//climber
		fastClimb.whileHeld(new ClimbUp(1));
		slowClimb.whileHeld(new ClimbUp(0.4));
		anticlimb.whileHeld(new AntiClimb());

		//test vision
		light.whileHeld(new TurnOnLight());
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

	public boolean driveSlow() {
		return slowDrive.get();
	}

	public boolean directionToggle() {
		if (toggleDirection.get() && !hasBeenPressed) {
			toggle = !toggle;
			hasBeenPressed = true;
		}

		if(!toggleDirection.get()) {
			hasBeenPressed = false;
		}
		return toggle;
	}

	public double getSpeed() {
		return (-rightStick.getZ() + 1)/2;
	}

	public JoystickButton slowClimb() {
		return slowClimb;
	}

	public JoystickButton fastClimb() {
		return fastClimb;
	}
}

