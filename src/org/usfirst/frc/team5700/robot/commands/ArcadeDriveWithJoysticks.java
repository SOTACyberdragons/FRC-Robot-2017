package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.SensitivityFilter;
import org.usfirst.frc.team5700.utils.SquareFilter;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveWithJoysticks extends Command {

	Preferences prefs = Preferences.getInstance();
	private double moveSensitivityThreshold;
	private double rotateSensitivityThreshold;

	public ArcadeDriveWithJoysticks() {
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
		Robot.drivetrain.reset();
	}

	protected void execute() {
		double moveValue = - Robot.oi.getRightStick().getY(); //forward joystick is negative, back is positive
		double rotateValue = - Robot.oi.getLeftStick().getX() * 0.7;
		SmartDashboard.putNumber("moveValue", moveValue);
		SmartDashboard.putNumber("rotateValue", rotateValue);

		moveSensitivityThreshold = prefs.getDouble("moveSensitivityThreshold", 0.05);
		rotateSensitivityThreshold = prefs.getDouble("rotateSensitivityThreshold", 0.05);

		SensitivityFilter moveSensitivityFilter = new SensitivityFilter(moveSensitivityThreshold);
		SensitivityFilter rotateSensitivityFilter = new SensitivityFilter(rotateSensitivityThreshold);

		//Robot.drivetrain.boostedArcadeDrive(moveValue, rotateValue);
		double filteredMoveValue = moveSensitivityFilter.output(moveValue);
		double filteredRotateValue = rotateSensitivityFilter.output(rotateValue);

		if (Robot.recordMode().equals("replay"))
			Robot.drivetrain.safeArcadeDriveDelayed(filteredMoveValue,
					filteredRotateValue);
		else
			Robot.drivetrain.safeArcadeDrive(filteredMoveValue,
					filteredRotateValue);

	}

	protected boolean isFinished() {
		return false;
	}
}
