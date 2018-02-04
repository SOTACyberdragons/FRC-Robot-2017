package org.usfirst.frc.team5700.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to
 * the robot's chassis. These include four drive motors, a left and right encoder
 * and a gyro.
 */
public class DriveTrain extends Subsystem {
	
	private SpeedController frontLeftMotor = new Spark(RobotMap.FRONT_LEFT_DRIVE_MOTOR);
	private SpeedController rearLeftMotor = new Spark(RobotMap.BACK_LEFT_DRIVE_MOTOR);
	private SpeedController frontRightMotor = new Spark(RobotMap.FRONT_RIGHT_DRIVE_MOTOR);
	private SpeedController rearRightMotor = new Spark(RobotMap.BACK_RIGHT_DRIVE_MOTOR);		

	private RobotDrive drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
	
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	private Encoder leftEncoder = new Encoder(1, 2, true);
	private Encoder rightEncoder = new Encoder(3, 4, false);

	//Encoder specs: S4T-360-250-S-D (usdigital.com)
	//S4T Shaft Encoder, 360 CPR, 1/4" Dia Shaft, Single-Ended, Default Torque
	//Encoder Distance Constants
    public static final double WHEEL_DIAMETER = 6; //Inches
    public static final double PULSE_PER_REVOLUTION = 360;
    public static final double ENCODER_GEAR_RATIO = 1;


    final double distancePerPulse = (WHEEL_DIAMETER * Math.PI) / PULSE_PER_REVOLUTION;
    
	private double angleRecord;
	private double distanceRecord;

	public DriveTrain() {
		super();

		//What is this distance??? 0.042?
		leftEncoder.setDistancePerPulse(distancePerPulse);
		rightEncoder.setDistancePerPulse(distancePerPulse);


		// Let's show everything on the LiveWindow
		LiveWindow.addActuator("Drive Train", "Front_Left Motor", (Spark) frontLeftMotor);
		LiveWindow.addActuator("Drive Train", "Back Left Motor", (Spark) rearLeftMotor);
		LiveWindow.addActuator("Drive Train", "Front Right Motor", (Spark) frontRightMotor);
		LiveWindow.addActuator("Drive Train", "Back Right Motor", (Spark) rearRightMotor);
		LiveWindow.addSensor("Drive Train", "Left Encoder", leftEncoder);
		LiveWindow.addSensor("Drive Train", "Right Encoder", rightEncoder);
		LiveWindow.addSensor("Drive Train", "Gyro", gyro);
	}

	/**
	 * When no other command is running let the operator drive around using the
	 * joysticks.
	 */
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
		SmartDashboard.putNumber("Left Distance", leftEncoder.getDistance());
		SmartDashboard.putNumber("Right Distance", rightEncoder.getDistance());
		SmartDashboard.putNumber("Left Speed", leftEncoder.getRate());
		SmartDashboard.putNumber("Right Speed", rightEncoder.getRate());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		
	}

	/**
	 * Arcade Drive
	 * @param rightStick joystick is for moving forwards and backwards
	 * @param leftStick joystick is for turning
	 */
	public void arcadeDrive(Joystick leftStick, Joystick rightStick, boolean squaredInputs) {
		double speed = Robot.oi.driveSlow() ? 0.6 : 1;
		double direction = Robot.oi.directionToggle() ? -1 : 1;
		drive.arcadeDrive(-rightStick.getY() * direction * speed, -leftStick.getX() * speed, squaredInputs);
	}	
	
	/**
	 * Tank Drive
	 * @param leftStick left set of wheels
	 * @param rightStick right set of wheels
	 */
	public void tankDrive(Joystick leftStick, Joystick rightStick, boolean squaredInputs) {
		drive.tankDrive(leftStick, rightStick, squaredInputs);
	}
	
	/**
	 * Tank style driving for the DriveTrain.
	 * 
	 * @param left
	 *            Speed in range [-1,1]
	 * @param right
	 *            Speed in range [-1,1]
	 */
	public void tankDrive(double left, double right) {
		drive.tankDrive(left, right);
	}

	/**
	 * @param joy
	 *            The joystick to use to drive tank style.
	 */
	public void tankDrive(Joystick joy) {
		tankDrive(-joy.getY(), -joy.getAxis(AxisType.kThrottle));
	}
	
	public void drive(double outputMagnitude, double curve) {
		drive.drive(outputMagnitude, curve);
	}
	/**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
		gyro.reset();
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	/**
	 * @return The robots heading in degrees.
	 */
	public double getHeading() {
		return gyro.getAngle();
	}

	/**
	 * @return The distance driven (average of left and right encoders).
	 */
	public double getDistance() {
		//return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
		return (rightEncoder.getDistance());
	}
	
	
	public PIDSource getGyro() {
		return gyro;
	}
	
	public void stop() {
		drive.drive(0.0, 0.0);
	}
	
	public void recordDrivenDistanceIn() {
		distanceRecord = this.getDistance();
	}
	
	public double getRecordedDistance() {
		return distanceRecord;
	}

	public void recordAngle() {
		angleRecord = this.getHeading();
	}
	
	public void addToRecordedAngle() {
		angleRecord += this.getHeading();
	}
	
	public double getRecordedAngle() {
		return angleRecord;
	}
	
}