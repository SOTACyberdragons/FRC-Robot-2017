package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.auto.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.auto.AutoMiddlePeg;
import org.usfirst.frc.team5700.robot.auto.AutoSidePeg;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.GearIntake;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private Command autonomousCommand;
	public static Preferences prefs;
	
	SendableChooser<Command> chooser;

	public static DriveTrain drivetrain;
	public static RopeClimber ropeClimber;
	public static GearIntake gearIntake;
	public static OI oi;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		prefs = Preferences.getInstance();
		
		// Initialize all subsystems
		drivetrain = new DriveTrain();
		ropeClimber = new RopeClimber();
		gearIntake = new GearIntake();
		oi = new OI();

		// instantiate the command used for the autonomous period
		chooser = new SendableChooser<Command>();
		chooser.addDefault("Cross Baseline", new AutoCrossBaseline());
		chooser.addObject("Middle Peg Auto", new AutoMiddlePeg());
		chooser.addObject("Right Peg Auto", new AutoSidePeg("right"));
		chooser.addObject("Left Peg Auto", new AutoSidePeg("left"));
		SmartDashboard.putData("Autonomous Chooser", chooser);
		SmartDashboard.putString("Selected Autonomous", chooser.getSelected().getName());
		autonomousCommand = chooser.getSelected();

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		SmartDashboard.putData(gearIntake);
		SmartDashboard.putData(ropeClimber);
	}

	@Override
	public void autonomousInit() {
		
		SmartDashboard.putString("Middle Peg", chooser.getSelected().getName());
		autonomousCommand = chooser.getSelected();
		autonomousCommand.start(); // schedule the autonomous command
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		log();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		log();
	}
	
	/**
	 * This function is called periodically dulring test mode
	 */
	@Override
	public void testPeriodic() {
		
		LiveWindow.run();
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	private void log() {
		drivetrain.log();
		//gearSystem.log();
		ropeClimber.log();
	}
}
