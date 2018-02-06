package org.usfirst.frc.team5700.utils;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class CyberPID extends PIDController {

	public CyberPID(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, setpoint, tolerance) {
		super(Kp, Ki, Kd, Kf, source, output);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
