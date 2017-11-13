package org.usfirst.frc.team5700.electronics;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDP {
	private static PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public static void resetEnergyRecord() {
		pdp.resetTotalEnergy();
	}
	
	public static double getTotalEnergy() {
		return pdp.getTotalEnergy();
	}
	
	public static double getCurrent(int channel) {
		return pdp.getCurrent(channel);
	}
	
	public static double getIntakeCurrent() {
		return pdp.getCurrent(RobotMap.INTAKE_PDP);
	}
}
