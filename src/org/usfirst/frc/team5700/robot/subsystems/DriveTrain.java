package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Constants;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;
import org.usfirst.frc.team5700.utils.BoostFilter;
import org.usfirst.frc.team5700.utils.BumpUpFilter;
import org.usfirst.frc.team5700.utils.ReluFilter;
import org.usfirst.frc.team5700.utils.SoftMaxFilter;
import org.usfirst.frc.team5700.utils.SquareFilter;

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

	public RobotDrive drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

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
	Preferences prefs = Preferences.getInstance();

	//input limiting fields
	private double previousMoveValue = 0;
	private double positiveInputChangeLimit;
	private double moveBoost;
	private double rotateBoost;
	private double requestedMoveChange;
	private double limitedMoveValue;
	private double negativeInputChangeLimit;
	private boolean positiveInputLimitActive;
	private boolean negativeInputLimitActive;

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
	 * Limits move input changes
	 * @param moveValue input for forward/backward motion
	 * @param rotateValue input for rotation
	 * 
	 * After change limit is applied, the input is passed to boosted arcadeDrive.
	 * Input from joystick should already be filtered for sensitivity
	 */
	public void safeArcadeDrive(double moveValue, double rotateValue) {
		requestedMoveChange = moveValue - previousMoveValue;
		limitedMoveValue = moveValue;
		positiveInputLimitActive = false;
		negativeInputLimitActive = false;

		boolean useInputLimit = prefs.getBoolean("useInputLimit", true);
		SmartDashboard.putBoolean("useInputLimit", useInputLimit);
		if (useInputLimit) {
			//check positive change
			positiveInputChangeLimit = prefs.getDouble("positiveInputChangeLimit", 0.025);
			negativeInputChangeLimit = prefs.getDouble("negativeInputChangeLimit", 0.025);
			if (requestedMoveChange > positiveInputChangeLimit) {
				positiveInputLimitActive = true;
				limitedMoveValue = previousMoveValue + positiveInputChangeLimit;

			}
			if (requestedMoveChange < - negativeInputChangeLimit) {
				negativeInputLimitActive = true;
				limitedMoveValue = previousMoveValue - negativeInputChangeLimit;
			}
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

		previousMoveValue = limitedMoveValue;
		boostedArcadeDrive(limitedMoveValue, rotateValue);

		SmartDashboard.putBoolean("positiveInputLimitActive", positiveInputLimitActive);
		SmartDashboard.putBoolean("negativeInputLimitActive", negativeInputLimitActive);
		SmartDashboard.putNumber("accelerometer -Y", - accel.getY());

	}
	
	public void safeArcadeDriveDelayed(double moveValue, double rotateValue) {
		Timer.delay(0.02);
		safeArcadeDrive(moveValue, rotateValue);
	}

	/**
	 * Applies BoostFilter to input
	 * @param moveValue input for forward/backward motion
	 * @param rotateValue input for rotation
	 * 
	 * After change limit is applied, the input is passed to boosted arcadeDrive
	 */
	public void boostedArcadeDrive(double moveValue, double rotateValue) {

		moveBoost = prefs.getDouble("moveBoost", 0.38);
		rotateBoost = prefs.getDouble("rotateBoost", 0.30);
		BoostFilter moveBoostFilter = new BoostFilter(moveBoost);
		BoostFilter rotateBoostFilter = new BoostFilter(rotateBoost);
		arcadeDrive(moveBoostFilter.output(moveValue), rotateBoostFilter.output(rotateValue));

	}
	public void arcadeDriveDelayed(double moveValue, double rotateValue) {
		Timer.delay(0.02);
		arcadeDrive(moveValue, rotateValue);
	}

	@SuppressWarnings("deprecation")
	public void arcadeDrive(double moveValue, double rotateValue) {

		double leftMotorSpeed;
		double rightMotorSpeed;
		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		
		double fastClimb = Robot.oi.fastClimb().get() ? 1.0 : 0.0;
		
		double filteredLeftMotorSpeed = SquareFilter.output(leftMotorSpeed);
		double filteredRightMotorSpeed = SquareFilter.output(rightMotorSpeed);

		Robot.csvLogger.writeData(
				timer.get(), 
				moveValue, //move input
				rotateValue, //rotate input
				filteredLeftMotorSpeed,
				filteredRightMotorSpeed,
				getAverageEncoderRate(),
				leftEncoder.getRate(),
				rightEncoder.getRate(),
				leftEncoder.getDistance(),
				rightEncoder.getDistance(),
				gyro.getAngle(),
				fastClimb
				);

		drive.tankDrive(filteredLeftMotorSpeed, filteredRightMotorSpeed, false); //squared input by default
		//drive.arcadeDrive(moveValue, rotateValue);
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


