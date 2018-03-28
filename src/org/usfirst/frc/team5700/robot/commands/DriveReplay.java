/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.CsvReader;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class DriveReplay extends Command {
	private CsvReader csvReader;
	private Iterator<float[]> valuesIterator;
	private Timer timer = new Timer();
	private float replayStartTime;
	private boolean timerStarted;
	private double offset;

	public DriveReplay() {
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {

		timer.reset();
		try {
			csvReader = new CsvReader(Robot.getReplayName());
			valuesIterator = csvReader.getValues().iterator();


			//System.out.println("move_value: " + nextLine[1] + ", rotate_value: " + nextLine[2]);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Init completed");
	}

	@Override
	protected void execute() {
		float[] nextLine;
		if (valuesIterator.hasNext()) {
			nextLine = valuesIterator.next();
			if (!timerStarted) {
				replayStartTime = nextLine[0];
				//System.out.println("Starting timer");
				timer.start();
				offset = replayStartTime - timer.get();
				System.out.println("Offset: " + offset);
				timerStarted = true;
			}
			
			double periodic_offset = Math.max(nextLine[0] - timer.get() - offset, 0);
			System.out.println("In execute, time difference: " + periodic_offset);

			Timer.delay(periodic_offset);
			Robot.drivetrain.arcadeDrive(nextLine[1], nextLine[2]);
		}
	}

	@Override
	protected boolean isFinished() {
		return !valuesIterator.hasNext();
	}

	@Override
	protected void end() {
		System.out.println("Replay ended");
		Robot.drivetrain.safeArcadeDrive(0, 0);
	}
}
