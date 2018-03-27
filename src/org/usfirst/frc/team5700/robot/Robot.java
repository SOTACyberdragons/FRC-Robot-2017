package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoMiddlePeg;
import org.usfirst.frc.team5700.robot.commands.AutoSidePeg;
import org.usfirst.frc.team5700.robot.commands.AutoTwoGear;
import org.usfirst.frc.team5700.robot.commands.DriveStraight;
import org.usfirst.frc.team5700.robot.commands.GetGearWithVision;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.GearIntake;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber;
import org.usfirst.frc.team5700.utils.CsvLogger;

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
	SendableChooser<String> stringChooser;
	String selectedString;

	public static DriveTrain drivetrain;
	public static RopeClimber ropeClimber;
	public static GearIntake gearIntake;
	public static OI oi;
	public static CsvLogger csvLogger;
	String[] data_fields ={"time",
			"move_value",
			"rotate_value",
			"average_encoder_rate",
			"accel_y"
			};

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		//always make sure we set vision to peg
		NetworkTable.getTable("vision").putString("model", "peg");
				
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
		chooser.addObject("Get Gear with Vision", new GetGearWithVision(true));
		SmartDashboard.putData("Autonomous Chooser", chooser);
		autonomousCommand = chooser.getSelected();

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		SmartDashboard.putData(gearIntake);
		SmartDashboard.putData(ropeClimber);
		SmartDashboard.putData("DriveStraight", new DriveStraight(prefs.getDouble("DriveStraight Distance", 200)));
		SmartDashboard.putData("DriveStraightToPeg", new DriveStraight(Dimensions.DISTANCE_TO_PEG-Dimensions.LENGTH_IN/2));

		System.out.println("Instantiating CsvLogger...");
		csvLogger = new CsvLogger();

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
		
		csvLogger.init(data_fields);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		SmartDashboard.putNumber("Throttled speed", oi.getSpeed());
		SmartDashboard.putNumber("Right stick Z", oi.getRightStick().getZ());
		log();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
		LiveWindow.run();
	}

	public void disabledInit() {
		
		csvLogger.close();
	}
	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	private void log() {
		//drivetrain.log();
		gearIntake.log();
		ropeClimber.log();
	}
}
