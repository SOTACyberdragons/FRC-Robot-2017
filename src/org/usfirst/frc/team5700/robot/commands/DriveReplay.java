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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private boolean timerStarted;
	private double timeOffset;
	private double kPReplay;
	private double leftDistanceOffset;
	private double rightDistanceOffset;

	public DriveReplay() {
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {

		timer.reset();
		timerStarted = false;
		kPReplay = Robot.prefs.getDouble("kPReplay", 0);
		Robot.prefs.putDouble("kPReplay", kPReplay);
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
	//			rightEncoder.getDistance()
	//			);
	
	@Override
	protected void execute() {
		float[] line;
		double leftEncoderDistance = Robot.drivetrain.getLeftEncoder().getDistance();
		double rightEncoderDistance = Robot.drivetrain.getRightEncoder().getDistance();
		if (valuesIterator.hasNext()) {
			line = valuesIterator.next();
			System.out.println("columns: " + line.length);
			if (!timerStarted) {
				timer.start();
				timeOffset = line[0] - timer.get();
				leftDistanceOffset = line[8] - leftEncoderDistance;
				rightDistanceOffset = line[9] - rightEncoderDistance;
				System.out.println("Offset: " + timeOffset);
				timerStarted = true;
			}
			
			double periodic_offset = Math.max(line[0] - timer.get() - timeOffset, 0);
			System.out.println("In execute, time difference: " + periodic_offset);

			double leftError = leftEncoderDistance - line[8] + leftDistanceOffset;
			double rightError = rightEncoderDistance - line[9] + rightDistanceOffset;
			double leftMotorSpeed = line[3] + kPReplay * leftError;
			double rightMotorSpeed = line[4] + kPReplay * rightError;
			SmartDashboard.putNumber("leftError", leftError);
			SmartDashboard.putNumber("rightError", rightError);
			
			System.out.println("Left motor output: " + leftMotorSpeed + ", right motor output: " + rightMotorSpeed);
			
			
			Timer.delay(periodic_offset);
			//Robot.drivetrain.arcadeDrive(nextLine[1], nextLine[2]);
			Robot.drivetrain.drive.tankDrive(leftMotorSpeed, rightMotorSpeed);
			
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
