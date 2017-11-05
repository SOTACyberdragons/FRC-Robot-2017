package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Dimensions;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;
import org.usfirst.frc.team5700.vision.BBoxLocator;
import org.usfirst.frc.team5700.vision.BBoxLocator.BBox;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author romandimov
 *
 */

public class GetGearWithVision extends Command {

	private PIDController pidAngle;

	private double angleKp;
	private double angleKi;
	private double angleKd;

	private double driveCurve = 0;
	private double driveOutput;

	private LinearAccelerationFilter filter;
	private boolean useAccelerationFiler;

	private BBoxLocator bBoxLocator = new BBoxLocator(Dimensions.GEAR_WIDTH_IN);

	private double distanceToGear;

	public GetGearWithVision(boolean useAccelerationFiler) {
		requires(Robot.drivetrain);

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

		LiveWindow.addActuator("drivetrain", "Gear Vision Angle Controller", pidAngle);
	}

	protected void initialize() {

		//Get PID values from preferences
		Preferences prefs = Preferences.getInstance();
		angleKp = prefs.getDouble("GetGear Kp", 0.01);
		angleKi = prefs.getDouble("GetGear Ki", 0.001);
		angleKd = prefs.getDouble("GetGear Kd", 0.0);
		driveOutput = prefs.getDouble("GetGear Speed", 0.5);
		distanceToGear = prefs.getDouble("GetGear D to Gear", 50);
		
		Robot.drivetrain.reset();
		pidAngle.reset();
		pidAngle.enable();

		double filterSlopeTime = prefs.getDouble("FilterSlopeTime", 0.2);

		filter = new LinearAccelerationFilter(filterSlopeTime);
		
		System.out.println("\n GetGearWithVision Initialized");
	}

	protected void execute() {
		//updates setpoint only if vision sees object
		Robot.gearIntake.gearIntakeDown();
		Robot.gearIntake.intakeGear();
		
		BBox bBox = bBoxLocator.getBBox();

		SmartDashboard.putNumber("PID Vision Setpoint Angle", pidAngle.getSetpoint());

		if (bBox != null) {
			pidAngle.setSetpoint(Robot.drivetrain.getHeading() + bBox.angleDeg);
		}

		Robot.drivetrain.drive(driveOutput * (useAccelerationFiler ? filter.output() : 1), driveCurve);
	}

	protected boolean isFinished() {
		return Robot.drivetrain.getDistance() > distanceToGear;
	}

	protected void end() {
		//Swtich models
		NetworkTable.getTable("vision").putString("model", "peg");
		
		//Lift intake
		Robot.gearIntake.gearIntakeUp();
		Robot.gearIntake.rollerHoldGear();

		//record driven distance
		Robot.drivetrain.recordDrivenDistanceIn();
		System.out.println();
		System.out.println("Drive to Gear Recorded Distance: " + Robot.drivetrain.getRecordedDistance());

		// Stop PID and the wheels
		pidAngle.disable();
		pidAngle.reset();
		Robot.drivetrain.drive(0, 0);
		Robot.drivetrain.reset();
		
		System.out.println("GetGearWithVision ended");
	}

	protected void interrupted() {
		end();
	}
}
