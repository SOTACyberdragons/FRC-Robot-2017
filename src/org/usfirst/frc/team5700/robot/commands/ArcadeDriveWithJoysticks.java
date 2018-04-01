package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.SensitivityFilter;

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
		Robot.drivetrain.safeArcadeDrive(moveSensitivityFilter.output(moveValue),
				rotateSensitivityFilter.output(rotateValue));
	}

	protected boolean isFinished() {
		return false;
	}
}
