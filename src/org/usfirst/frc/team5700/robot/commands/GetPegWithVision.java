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

	private double angleKp = 0.004;
	private double angleKi = 0.000001;
	private double angleKd = 0.001;

	private double driveCurve = 0;
	private double driveOutput;

	private LinearAccelerationFilter filter;

	private boolean useAccelerationFiler;

	private BBoxLocator bBoxLocator = new BBoxLocator(Dimensions.TAPE_WIDTH_IN);

	Preferences prefs = Preferences.getInstance();

	private boolean waitForVision;

	private boolean wasDetected;

	public GetPegWithVision(boolean useAccelerationFiler, boolean waitForVision) {
		requires(Robot.drivetrain);

		this.waitForVision = waitForVision;

		//driveOutput = Robot.prefs.getDouble("Drive with Vision Speed", 0.6);
		driveOutput = prefs.getDouble("Drive with Vision Speed", 0.5);

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

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();

		Robot.gearIntake.lightOn();

		double filterSlopeTime = prefs.getDouble("FilterSlopeTime", 0.2);

		filter = new LinearAccelerationFilter(filterSlopeTime);

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//updates setpoint only if vision sees object
		BBox bBox = bBoxLocator.getBBox();
		SmartDashboard.putBoolean("Can See With Vision", wasDetected);

		if (bBox != null) {
			wasDetected = true;
			pidAngle.setSetpoint(Robot.drivetrain.getHeading() + bBox.angleDeg);
			SmartDashboard.putNumber("PID Vision Setpoint Angle", pidAngle.getSetpoint());
		}

		if (waitForVision) {
			Robot.drivetrain.stop();
			if (wasDetected) {
				Robot.drivetrain.drive(driveOutput * (useAccelerationFiler ? filter.output() : 1), driveCurve);
			}
		} else {
			Robot.drivetrain.drive(driveOutput * (useAccelerationFiler ? filter.output() : 1), driveCurve);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		//switch off when peg pushes flap
		return Robot.gearIntake.getPegSwitch();
	}

	// Called once after isFinished returns true
	protected void end() {

		NetworkTable.getTable("vision").putString("model", "gear");

		// Stop PID and the wheels
		pidAngle.disable();
		pidAngle.reset();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();

		Robot.gearIntake.lightOff();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
