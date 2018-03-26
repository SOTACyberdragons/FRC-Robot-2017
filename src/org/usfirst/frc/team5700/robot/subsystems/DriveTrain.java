package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Constants;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;
import org.usfirst.frc.team5700.utils.BumpUpFilter;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {

	public static double maxSpeed; //TODO find
	public static double maxForwardAccel;
	public static double maxBackwardAccel;
	public static double MAX_SIDE_ACCEL;
	public double previousSpeedInput = 0;

	private SpeedController frontLeftMotor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
	private SpeedController rearLeftMotor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
	private SpeedController frontRightMotor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
	private SpeedController rearRightMotor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);		

	private RobotDrive drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

	private BuiltInAccelerometer accel = new BuiltInAccelerometer();

	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	Timer timer = new Timer();

	//Encoder specs: S4T-360-250-S-D (usdigital.com)
	//S4T Shaft Encoder, 360 CPR, 1/4" Dia Shaft, Single-Ended, Default Torque
	//Encoder Distance Constants
	public final static double WHEEL_BASE_WIDTH_IN = 25; //TOOD find
	public final static double WHEEL_DIAMETER = 6;
	public final static double PULSE_PER_REVOLUTION = 360;

	private Encoder leftEncoder = new Encoder(1, 2, true);
	private Encoder rightEncoder = new Encoder(3, 4, false);
	private double distanceRecord;
	private double angleRecord;

	final static double distancePerPulseIn = Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION;

	public DriveTrain() {
		leftEncoder.setDistancePerPulse(distancePerPulseIn);
		rightEncoder.setDistancePerPulse(distancePerPulseIn);
		timer.start();

		//leftMotor.setInverted(false);
		//rightMotor.setInverted(false);
		frontLeftMotor.setInverted(false);
		frontRightMotor.setInverted(false);
		rearLeftMotor.setInverted(false);
		rearRightMotor.setInverted(false);

		reset();
	}


	/**
	 * Arcade Drive
	 * @param rightStick joystick is for moving forwards and backwards
	 * @param leftStick joystick is for turning
	 */
	public void arcadeDrive(Joystick leftStick, Joystick rightStick) {

		double rightStickY = rightStick.getY();
		double leftStickX = leftStick.getX();
		double averageEncoderRate = getAverageEncoderRate();
		double accelY = accel.getY();
		double accelX = accel.getX();
		double time = timer.get();
		Preferences prefs = Preferences.getInstance();
		boolean squaredInputs = prefs.getBoolean("squaredInputs", false);
		BumpUpFilter bumpUpFilter = new BumpUpFilter();

		//		SmartDashboard.putNumber("averageEncoderRate", averageEncoderRate);
		//		SmartDashboard.putNumber("rightStickY", rightStickY);
		//		SmartDashboard.putNumber("leftStickX", leftStickX);
		//		SmartDashboard.putNumber("accelX", accelX);
		//		SmartDashboard.putNumber("accelY", accelY);
		//		SmartDashboard.putNumber("time", timer.get());

		//		data fields for reference		
		//		String[] data_fields ={"time",
		//				"average_encoder_rate",
		//				"right_stick_y",
		//				"accel_y"
		//				};

		Robot.csvLogger.writeData(time, averageEncoderRate, rightStickY, accelY);
		double bumpedY = bumpUpFilter.output(rightStickY);

		drive.arcadeDrive(bumpedY, leftStickX, squaredInputs);	
	}

	public void safeArcadeDrive(double moveValue, double rotateValue) {

		double currentSpeed = getAverageEncoderRate(); //may be positive or negative
		double newRotateValue = rotateValue;
		boolean forwardAccelLimit = false;
		boolean backwardAccelLimit = false;
		double accelY = accel.getY();
		double accelX = accel.getX();
		double time = timer.get();

		Preferences prefs = Preferences.getInstance();
		boolean squaredInputs = prefs.getBoolean("squaredInputs", false);
		boolean useAccelLimit = prefs.getBoolean("useAccelLimit", true);
		
		//Bump-up filter parameters
		double exp = prefs.getDouble("exp", 200);
		double threshold = prefs.getDouble("threshold", 0.04);
		double target = prefs.getDouble("target", 0.3);

		BumpUpFilter bumpUpFilter = new BumpUpFilter(exp, threshold, target);
		double newMoveValue = bumpUpFilter.output(moveValue); //convert to desired joystick response

		if (useAccelLimit) {

			//prevent getting 0 or negative values from prefs
			maxForwardAccel = Math.max(prefs.getDouble("maxForwardAccel", 60), 5); //0.3 g -> 116 in/s^2
			maxBackwardAccel = Math.max(prefs.getDouble("maxBackwardAccel", 60), 5); //0.3 g -> 116 in/s^2
			maxSpeed = Math.max(prefs.getDouble("maxSpeed", 120), Math.abs(currentSpeed)); //10 ft/s

			double requestedSpeedChange = moveValue * maxSpeed - currentSpeed;
			SmartDashboard.putNumber("requestedSpeedChange", requestedSpeedChange);

			if (requestedSpeedChange > maxForwardAccel * Constants.CYCLE_SEC) {
				newMoveValue = Math.min(previousSpeedInput + (maxForwardAccel * Constants.CYCLE_SEC / maxSpeed ),
						moveValue);
				forwardAccelLimit = true;

			} else if (requestedSpeedChange < -maxBackwardAccel * Constants.CYCLE_SEC) {
				newMoveValue = Math.max(previousSpeedInput - (maxBackwardAccel * Constants.CYCLE_SEC / maxSpeed),
						moveValue);
				backwardAccelLimit = true;
			}


			//rotational accel.

			//		double turnRadiusIn = -Math.log(newTurnInput) * WHEEL_BASE_WIDTH_IN;
			//		MAX_SIDE_ACCEL = prefs.getDouble("Max Side Accel", 60);
			//		double radiusThreshhold = prefs.getDouble("radius threshhold", 10);
			//		double wantedSideAccel = Math.pow(currentSpeedInPerSec, 2) / turnRadiusIn;
			//
			//		if (turnRadiusIn > radiusThreshhold && wantedSideAccel > MAX_SIDE_ACCEL) {
			//			newTurnInput = Math.exp((-Math.pow(currentSpeedInPerSec, 2)/MAX_SIDE_ACCEL)/WHEEL_BASE_WIDTH_IN);
			//			setOverrideTurnStick(true);
			//		}

			//		SmartDashboard.putNumber("MAX_FORWARD_ACCEL", MAX_FORWARD_ACCEL);
			//		SmartDashboard.putNumber("MAX_BACKWARD_ACCEL", MAX_BACKWARD_ACCEL);
			//		SmartDashboard.putNumber("MAX_SPEED_IN_PER_SEC", MAX_SPEED_IN_PER_SEC);
			//		SmartDashboard.putNumber("X Acceleration", accel.getX());
			//		SmartDashboard.putNumber("Y Acceleration", accel.getY());

			previousSpeedInput = newMoveValue;
		}
		
		SmartDashboard.putBoolean("forwardAccelLimit", forwardAccelLimit);
		SmartDashboard.putBoolean("backwardAccelLimit", backwardAccelLimit);
		SmartDashboard.putNumber("currentSpeed", currentSpeed);
		SmartDashboard.putNumber("moveValue", moveValue);

		Robot.csvLogger.writeData(time, currentSpeed, moveValue, accelY);

		drive.arcadeDrive(newMoveValue, newRotateValue, squaredInputs);
	}



	public void arcadeDrive(double outputMagnitude, double curve) {
		drive.arcadeDrive(-outputMagnitude, -curve);
	}

	public void tankDrive(double left, double right) {
		//rightMotor.set(right);
		//leftMotor.set(-left + 0.065);
	}

	public void stop() {
		drive.drive(0.0, 0.0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}

	public double getXAccel() {
		return accel.getX();
	}

	public double getYAccel() {
		return accel.getY();
	}

	public double getZAccel() {
		return accel.getZ();
	}

	public void recordDrivenDistanceIn() {
		distanceRecord = this.getDistance();
	}

	public void recordAngle() {
		angleRecord = this.getHeading();
	}

	public void reset() {
		gyro.reset();
		leftEncoder.reset();
		rightEncoder.reset();
	}

	public double getDistance() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	/**
	 * @return rate, ticks per second
	 */
	public double getAverageEncoderRate() {
		return ((leftEncoder.getRate() + rightEncoder.getRate())/2);
	}

	public double getHeading() {
		return gyro.getAngle();
	}

	public Encoder getRightEncoder() {
		return rightEncoder;
	}

	public Encoder getLeftEncoder() {
		return leftEncoder;
	}

	public PIDSource getGyro() {
		return gyro;
	}

	public void drive(double outputMagnitude, double curve) {
		drive.drive(outputMagnitude, curve);
	}

	public double getRecordedDistance() {
		return distanceRecord;
	}

	public double getRecordedAngle() {
		return angleRecord;
	}

	public void addToRecordedAngle() {
		angleRecord += this.getHeading();
	}
}


