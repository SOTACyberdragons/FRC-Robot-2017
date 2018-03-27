/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.CsvReader;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class DriveReplay extends Command {
	private CsvReader csvReader;
	private Iterator<float[]> valuesIterator;

	public DriveReplay() {
		requires(Robot.drivetrain);
	}
	
	@Override
	protected void initialize() {
		try {
			csvReader = new CsvReader();
			valuesIterator = csvReader.getValues().iterator();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void execute() {
		float[] nextLine;
		if (valuesIterator.hasNext()) {
			nextLine = valuesIterator.next();
			Robot.drivetrain.safeArcadeDrive(nextLine[1], nextLine[2]);
		}
	}

	@Override
	protected boolean isFinished() {
		return valuesIterator.hasNext();
	}

	@Override
	protected void end() {
		Robot.drivetrain.safeArcadeDrive(0, 0);
	}
}
