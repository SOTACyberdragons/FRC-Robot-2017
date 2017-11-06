package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Dimensions;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;
import org.usfirst.frc.team5700.vision.BBoxLocator;
import org.usfirst.frc.team5700.vision.BBoxLocator.BBox;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author romandimov
 *
 */

public class GetPegWithVision extends Command {

	private PIDController pidAngle;

	private double angleKp;
	private double angleKi;
	private double angleKd;

	private double driveCurve = 0;
	private double driveSpeed;

	private LinearAccelerationFilter filter;
	private boolean useAccelerationFiler;

	private BBoxLocator bBoxLocator = new BBoxLocator(Dimensions.TAPE_WIDTH_IN);

	private boolean wasDetected;

	private boolean waitForVision;

	public GetPegWithVision(boolean useAccelerationFiler, boolean waitForVision) {
		requires(Robot.drivetrain);

		this.waitForVision = waitForVision;

		pidAngle = new PIDController(angleKp,
				angleKi,
				angleKd, 
				Robot.drivetrain.getGyro(), 
				new PIDOutput() {
			@Override
			public void pidWrite(double c) {
				driveCurve = c;
			}
		});

		pidAngle.setOutputRange(-1.0, 1.0);

		LiveWindow.addActuator("drivetrain", "Peg Vision Angle Controller", pidAngle);
	}

	protected void initialize() {

		//Get PID values from preferences
		Preferences prefs = Preferences.getInstance();
		angleKp = prefs.getDouble("GetPeg Kp", 0.01);
		angleKi = prefs.getDouble("GetPeg Ki", 0.001);
		angleKd = prefs.getDouble("GetPeg Kd", 0.0);
		driveSpeed = prefs.getDouble("GetPeg Speed", 0.5);
		
		Robot.drivetrain.reset();
		Robot.drivetrain.stop();
		pidAngle.reset();
		pidAngle.enable();

		Robot.gearIntake.lightOn();

		double filterSlopeTime = prefs.getDouble("FilterSlopeTime", 0.2);

		filter = new LinearAccelerationFilter(filterSlopeTime);
		
		System.out.println("\n GetPegWithVision Initialized");
	}

	protected void execute() {
		//updates setpoint only if vision sees object
		BBox bBox = bBoxLocator.getBBox();
		SmartDashboard.putBoolean("Can See With Vision", wasDetected);

		if (bBox != null) {
			wasDetected = true;
			pidAngle.setSetpoint(Robot.drivetrain.getHeading() + bBox.angleDeg);
			SmartDashboard.putNumber("PID Vision Setpoint Angle", pidAngle.getSetpoint());
		}

		if (!waitForVision || wasDetected) {
			Robot.drivetrain.drive(driveSpeed * (useAccelerationFiler ? filter.output() : 1), driveCurve);
		}
	}

	protected boolean isFinished() {
		//switch off when peg pushes flap
		return Robot.gearIntake.getPegSwitch();
	}

	protected void end() {

		NetworkTable.getTable("vision").putString("model", "gear");

		// Stop PID and the wheels
		pidAngle.disable();
		pidAngle.reset();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();

		Robot.gearIntake.lightOff();
	}
	
	protected void interrupted() {
		end();
	}
}
