package org.usfirst.frc.team5700.utils;

import org.usfirst.frc.team5700.robot.Robot;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class NetworkTableSensor implements PIDSource {

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return null;
	}

	public double pidGet() {
		return Robot.networkTable.getNumber("x", 0);
	}
}
