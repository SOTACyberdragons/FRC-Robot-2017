package org.usfirst.frc.team5700.utils;

import edu.wpi.first.wpilibj.Timer;

public class LinearAccelerationFilter {
	
	Timer timer;
	boolean firstTick = true;
	
	double duration;
	
	
	/**
	 * @param duration of acceleration period, in seconds
	 */
	public LinearAccelerationFilter(double duration) {
		this.duration = duration;
		timer = new Timer();
	}
	
	public double output() {
		if (firstTick) {
			firstTick = false;
			timer.start();
		}
		double time = timer.get();
		double output;
		
		if (time > duration) 
			output = 1.0;
		else		
			output = timer.get()/duration;
		
		//System.out.println("LinearAcceleratorOutput: " + output);
		return output;
	}

}
