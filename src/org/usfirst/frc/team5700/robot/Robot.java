package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.electronics.PDP;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoMiddlePeg;
import org.usfirst.frc.team5700.robot.commands.AutoSidePeg;
import org.usfirst.frc.team5700.robot.commands.AutoTwoGear;
import org.usfirst.frc.team5700.robot.commands.DriveStraightToDistance;
import org.usfirst.frc.team5700.robot.commands.TurnAngle;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.GearIntake;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
		
		//always make sure we set vision to peg
		NetworkTable.getTable("vision").putString("model", "peg");
		
		//Reset PDP energy record
		PDP.resetEnergyRecord();
				
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
		chooser.addObject("Two Gear to Right", new AutoTwoGear("right"));
		chooser.addObject("Two Gear to Left", new AutoTwoGear("left"));
		SmartDashboard.putData("Autonomous Chooser", chooser);
		autonomousCommand = chooser.getSelected();

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		SmartDashboard.putData(gearIntake);
		SmartDashboard.putData(ropeClimber);
		SmartDashboard.putData("DriveStraight", new DriveStraightToDistance(prefs.getDouble("DriveStraight Distance", 200)));
		SmartDashboard.putData("DriveStraightToPeg", new DriveStraightToDistance(Dimensions.DISTANCE_TO_PEG-Dimensions.LENGTH_IN/2));
		SmartDashboard.putData("Turn 90 Deg", new TurnAngle(90, false));
	}

	@Override
	public void autonomousInit() {
		
		SmartDashboard.putString("Autonomous Mode: ", chooser.getSelected().getName());
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
		autonomousCommand.cancel();
		//always make sure we set vision to peg
		NetworkTable.getTable("vision").putString("model", "peg");
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		log();
		
		SmartDashboard.putNumber("Total Energy Used", PDP.getTotalEnergy());
	}
	
	/**
	 * This function is called periodically during test mode
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
		gearIntake.log();
		ropeClimber.log();
	}
}
