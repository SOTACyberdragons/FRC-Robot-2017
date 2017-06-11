package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.DriveAndPlaceGear;
import org.usfirst.frc.team5700.robot.commands.DriveAndPlaceGearSide;
import org.usfirst.frc.team5700.robot.commands.DriveStraight;
import org.usfirst.frc.team5700.robot.commands.GearDropAutomatic;
import org.usfirst.frc.team5700.robot.commands.NetworkTableTest;
import org.usfirst.frc.team5700.robot.commands.Turn;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.GearSystem;
import org.usfirst.frc.team5700.robot.subsystems.RopeClimber;
import org.usfirst.frc.team5700.utils.NetworkTableSensor;
import org.usfirst.frc.team5700.utils.Side;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
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
	Command autonomousCommand;
	//Command networkTableTest;
	Preferences prefs;
	
	SendableChooser<Command> chooser;

	public static DriveTrain drivetrain;
	//public static PidDriveTrain pidDrivetrain;
	public static RopeClimber ropeClimber;
	public static GearSystem gearSystem;
	public static OI oi;
	public static CameraServer cameraserver;
	UsbCamera camera;
	
	private Timer gearDropTimer;
	
	public static NetworkTable networkTable;
	public static NetworkTableSensor networkTableSensor;
	
    public static boolean wasPressed = false;
	public static GearDropAutomatic gearDropAutomatic;
	int exposure;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		prefs = Preferences.getInstance();
		
		networkTable = NetworkTable.getTable("GRIP/grip");
		//networkTableTest = new NetworkTableTest();
		networkTableSensor = new NetworkTableSensor();
		
		System.out.println("In robotInit");
		
		exposure = prefs.getInt("Exposure", 20);

		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setFPS(prefs.getInt("FPS", 8));
		camera.setResolution(RobotMap.CAMERA_WIDTH, RobotMap.CAMERA_HEIGHT);
		camera.setExposureManual(exposure);
		SmartDashboard.putNumber("Exposure value", exposure);
		SmartDashboard.putString("Resolution", RobotMap.CAMERA_WIDTH + "x" + RobotMap.CAMERA_HEIGHT);
		SmartDashboard.putNumber("Angle per Pixel", RobotMap.ANGLE_PER_PIXEL);
		SmartDashboard.putNumber("X Angle", RobotMap.ANGLE_PER_PIXEL * 160);
		
		gearDropTimer = new Timer();
		
		double distance = prefs.getDouble("Auto Distance", 24.0);
		
		// Initialize all subsystems
		drivetrain = new DriveTrain();
		ropeClimber = new RopeClimber();
		gearSystem = new GearSystem();
		oi = new OI();

		// instantiate the command used for the autonomous period
		
		chooser = new SendableChooser<Command>();
		chooser.addDefault("Cross Baseline", new DriveStraight(distance));
		chooser.addObject("Place Gear Middle", new DriveAndPlaceGear());
		chooser.addObject("Place Gear Right ", new DriveAndPlaceGearSide(Side.RIGHT));
		chooser.addObject("Place Gear Left", new DriveAndPlaceGearSide(Side.LEFT));
		SmartDashboard.putData("Autonomous Chooser", chooser);
		SmartDashboard.putString("Selected Autonomous", chooser.getSelected().getName());
		autonomousCommand = chooser.getSelected();

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		SmartDashboard.putData(gearSystem);
		SmartDashboard.putData(ropeClimber);
		
		double angle = prefs.getDouble("Test Turn Angle", 60.0);
		SmartDashboard.putData("Drive Straight", new DriveStraight(distance));
		SmartDashboard.putData("Turn", new Turn(angle));
    	SmartDashboard.putBoolean("Gear Placed", false);
	}

	@Override
	public void autonomousInit() {
		System.out.println("In autonomousInit");
		SmartDashboard.putString("Selected Autonomous", chooser.getSelected().getName());
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
		//networkTableTest.start();
		
		
		camera.setExposureManual(exposure);
		SmartDashboard.putNumber("Exposure value", exposure);
		camera.setFPS(prefs.getInt("FPS", 8));
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		//throttle range: -1.0..1.0
		//usb camera manual exposure range: 0..100
		//int exposure = (int) (100 * (oi.getLeftStick().getZ() + 1.0) / 2.0); //throttle ranges -1.0..1.0
		//camera.setExposureManual(exposure);
		//SmartDashboard.putNumber("Exposure", exposure);
		
		double[] defaultValue = new double[0];
		double[] centerXarray = networkTable.getNumberArray("centerX", defaultValue);
		double centerX = centerXarray.length < 1 ? 0 : (centerXarray[0]);
		SmartDashboard.putNumber("Center X pixels", centerX);
		SmartDashboard.putNumber("Center X angle", centerX * RobotMap.ANGLE_PER_PIXEL);
		SmartDashboard.putNumber("Angle from Center", - (centerX - RobotMap.CAMERA_WIDTH / 2.0) * RobotMap.ANGLE_PER_PIXEL);
		
		log();
		//if (Robot.oi.getGearDropBlock().get())
		//	gearSystem.resetSwitchCount();
//        if (!Robot.oi.getGearDropBlock().get() && gearSystem.gearSwitchPushed() && !wasPressed) {
		if (gearSystem.gearSwitchPushed() && !wasPressed) {
        	//SmartDashboard.putBoolean("Run Drop Command", true);
        	wasPressed = true;
        	gearDropTimer.start();
        	SmartDashboard.putBoolean("Gear Trigger Pushed", true);
//        	gearDropAutomatic = new GearDropAutomatic();
//        	gearDropAutomatic.start();			
        } else {
        	if (gearDropTimer.get() > 1.0) {
        		gearDropTimer.reset();
        		wasPressed = false;
        		gearSystem.resetSwitchCount();
        		SmartDashboard.putBoolean("Gear Trigger Pushed", false);
        	}
        }
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
		gearSystem.log();
		ropeClimber.log();
	}
}
