package org.usfirst.frc.team5700.robot;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoMiddlePeg;
import org.usfirst.frc.team5700.robot.commands.AutoSidePeg;
import org.usfirst.frc.team5700.robot.commands.AutoTwoGear;
import org.usfirst.frc.team5700.robot.commands.DriveReplay;
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
	SendableChooser<String> replayChooser;
	String selectedString;

	public static DriveTrain drivetrain;
	public static RopeClimber ropeClimber;
	public static GearIntake gearIntake;
	public static OI oi;
	public static CsvLogger csvLogger;

	
	//		Robot.csvLogger.writeData(time, moveValue, rotateValue, newMoveValue, newRotateValue, 
//	currentSpeed, requestedSpeedChange, forwardAccelLimit ? 1 : 0, backwardAccelLimit ? 1 : 0, maxSpeed);
	String[] data_fields ={
			"time",
			"moveValue",
			"rotateValue",
			"newMoveValue",
			"newRotateValue",
			"currentSpeed",
			"requestedSpeedChange",
			"forwardAccelLimit",
			"backwardAccelLimit",
			"accelY",
			"maxSpeed"
	};

	private String recordMode;
	private boolean teleopRan;
	private boolean autoRan;
	private static String replayName;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		//always make sure we set vision to peg
		NetworkTable.getTable("vision").putString("model", "peg");
		System.out.println("Robot init...");

		prefs = Preferences.getInstance();

		// Initialize all subsystems
		drivetrain = new DriveTrain();
		ropeClimber = new RopeClimber();
		gearIntake = new GearIntake();
		oi = new OI();

		// instantiate the command used for the autonomous period
		chooser = new SendableChooser<Command>();
		chooser.addObject("Cross Baseline", new AutoCrossBaseline());
		chooser.addObject("Middle Peg Auto", new AutoMiddlePeg());
		chooser.addObject("Right Peg Auto", new AutoSidePeg("right"));
		chooser.addObject("Left Peg Auto", new AutoSidePeg("left"));
		chooser.addObject("Two Gear to Right", new AutoTwoGear("right"));
		chooser.addObject("Two Gear to Left", new AutoTwoGear("left"));
		chooser.addObject("Get Gear with Vision", new GetGearWithVision(true));
		chooser.addDefault("Replay", new DriveReplay());
		SmartDashboard.putData("Autonomous Chooser", chooser);
		autonomousCommand = chooser.getSelected();

		setupRecordMode();

		listReplays();


		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		SmartDashboard.putData(gearIntake);
		SmartDashboard.putData(ropeClimber);
		SmartDashboard.putData("DriveStraight", new DriveStraight(prefs.getDouble("DriveStraight Distance", 200)));
		SmartDashboard.putData("DriveStraightToPeg", new DriveStraight(Dimensions.DISTANCE_TO_PEG-Dimensions.LENGTH_IN/2));

		System.out.println("Instantiating CsvLogger...");
		csvLogger = new CsvLogger();

	}

	private void setupRecordMode() {
		stringChooser = new SendableChooser<String>();
		stringChooser.addDefault("Just Drive", "justDrive");
		stringChooser.addObject("Replay", "replay");
		SmartDashboard.putData("RecordMode", stringChooser);
		SmartDashboard.putString("Replay Name", "MyReplay");
		recordMode = stringChooser.getSelected();
	}

	private void listReplays() {
		System.out.println("Listing replays...");
		replayChooser = new SendableChooser<String>();
		Iterator<Path> replayFiles = null;
		try {
			replayFiles = Files.newDirectoryStream(Paths.get(Constants.dataDir), "*.rpl").iterator();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (replayFiles.hasNext()) {
			String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
			replayChooser.addDefault(replayFile, replayFile);
		}
		while (replayFiles.hasNext()) {
			String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
			System.out.println(replayFile);
			replayChooser.addObject(replayFile, replayFile);
		}
		SmartDashboard.putData("ReplaySelector", replayChooser);
	}

	@Override
	public void autonomousInit() {
		autoRan = true;
		replayName = replayChooser.getSelected();

		csvLogger.init(data_fields, Constants.dataDir, false, null);

		autonomousCommand = chooser.getSelected();
		SmartDashboard.putString("Autonomous Command: ", autonomousCommand.getName());
		System.out.println("Autonomous command: " + autonomousCommand.getName());
		System.out.println("Selected replay: " + replayName);

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
		teleopRan = true;
		autonomousCommand.cancel();
		SmartDashboard.putString("Autonomous Mode: ", chooser.getSelected().getName());
		//always make sure we set vision to peg
		NetworkTable.getTable("vision").putString("model", "peg");
		listReplays();

		recordMode = stringChooser.getSelected();

		String newReplayName = SmartDashboard.getString("Replay Name", "MyReplay");
		csvLogger.init(data_fields, Constants.dataDir, recordMode.equals("replay"), newReplayName);
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
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

		LiveWindow.run();
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {

		if (teleopRan) {
			System.out.println("Disabling robot after a teleop run...");
			listReplays();
			setupRecordMode();
			teleopRan = false;
		}
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

	public static String getReplayName() {
		// TODO Auto-generated method stub
		return replayName;
	}
}
