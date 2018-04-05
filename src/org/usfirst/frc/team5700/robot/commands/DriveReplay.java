/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.AutoControls;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.CsvReader;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class DriveReplay extends Command {
	private CsvReader csvReader;
	private Iterator<double[]> valuesIterator;
	private Timer timer = new Timer();
	private boolean timerStarted;
	private double timeOffset;
	private double replayKProp;
	private double leftDistanceOffset;
	private double rightDistanceOffset;
	private double leftError;
	private double rightError;
	private double leftErrorChange;
	private double rightErrorChange;
	private double lastLeftError = 0;
	private double lastRightError = 0;
	private double replayKDiff;
	private double headingOffset;
	private double headingError;
	private double replayKHeadingProp;
	private double lastHeadingError;
	private double headingErrorChange;
	private double replayKHeadingDiff;

	public DriveReplay() {
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {

		timer.reset();
		timerStarted = false;
		Robot.drivetrain.reset();
		replayKProp = Robot.prefs.getDouble("replayKProp", 0);
		Robot.prefs.putDouble("replayKProp", replayKProp);
		replayKDiff = Robot.prefs.getDouble("replayKDiff", 0);
		Robot.prefs.putDouble("replayKDiff", replayKDiff);
		replayKHeadingProp = Robot.prefs.getDouble("replayKHeadingProp", 0);
		Robot.prefs.putDouble("replayKHeadingProp", replayKHeadingProp);
		replayKHeadingDiff = Robot.prefs.getDouble("replayKHeadingDiff", 0);
		Robot.prefs.putDouble("replayKHeadingDiff", replayKHeadingDiff);
		
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

	//	Robot.csvLogger.writeData(
	//			timer.get(), 
	//			moveValue, //move input
	//			rotateValue, //rotate input
	//			leftMotorSpeed,
	//			rightMotorSpeed,
	//			getAverageEncoderRate(),
	//			leftEncoder.getRate(),
	//			rightEncoder.getRate(),
	//			leftEncoder.getDistance(),
	//			rightEncoder.getDistance(),
	//			gyro.getAngle()
	//			);
	
	@Override
	protected void execute() {
		double[] line;
		double leftEncoderDistance = Robot.drivetrain.getLeftEncoder().getDistance();
		double rightEncoderDistance = Robot.drivetrain.getRightEncoder().getDistance();
		double heading = Robot.drivetrain.getHeading();
		if (valuesIterator.hasNext()) {
			line = valuesIterator.next();
			System.out.println("columns: " + line.length);
			if (!timerStarted) {
				timer.start();
				timeOffset = line[0] - timer.get();
				leftDistanceOffset = line[8] - leftEncoderDistance;
				rightDistanceOffset = line[9] - rightEncoderDistance;
				headingOffset = line[10] - heading;
				System.out.println("Offset: " + timeOffset);
				timerStarted = true;
			}
			
			double periodic_offset = Math.max(line[0] - timer.get() - timeOffset, 0);
			System.out.println("In execute, time difference: " + periodic_offset);

			leftError = leftEncoderDistance - line[8] + leftDistanceOffset;
			rightError = rightEncoderDistance - line[9] + rightDistanceOffset;
			headingError = heading - line[10] + headingOffset;
			leftErrorChange = lastLeftError - leftError;
			rightErrorChange = lastRightError - rightError;
			headingErrorChange = lastHeadingError - headingError;
			lastLeftError = leftError;
			lastRightError = rightError;
			lastHeadingError = headingError;
			
			double leftMotorSpeed = line[3] + replayKProp * leftError - replayKDiff * leftErrorChange
					- replayKHeadingProp * headingError - replayKHeadingDiff * headingErrorChange;
			double rightMotorSpeed = line[4] + replayKProp * rightError - replayKDiff * rightErrorChange
					+ replayKHeadingProp * headingError + replayKHeadingDiff * headingErrorChange;
			
			SmartDashboard.putNumber("leftError", leftError);
			SmartDashboard.putNumber("rightError", rightError);
			SmartDashboard.putNumber("headingError", headingError);

			boolean fastClimb =  line[11] != 0.0;
			AutoControls.setFastClimb(fastClimb);
			SmartDashboard.putBoolean("fastClimb", fastClimb);
			
			
			System.out.println("Left motor output: " + leftMotorSpeed + ", right motor output: " + rightMotorSpeed);
			
			
			Timer.delay(periodic_offset);
			//Robot.drivetrain.arcadeDrive(nextLine[1], nextLine[2]);
			Robot.drivetrain.drive.tankDrive(leftMotorSpeed, rightMotorSpeed, false); //disable squared
			
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
		timer.stop();
		timer.reset();
	}
}
