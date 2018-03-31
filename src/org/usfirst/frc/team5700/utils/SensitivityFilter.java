package org.usfirst.frc.team5700.utils;

/**
 * DESCRIPTION: <br>
 * Keeps input at 0 up to the sensitivity threshold then behaves linearly
 * 
 */

public class SensitivityFilter {
	
	private double threshold;
	
	public SensitivityFilter(double threshold) {
		
		this.threshold = threshold;
		
	}

	
	public double output(double input) {
		
		double magn = Math.abs(input);
		
		return magn < threshold ? 0 : (input - threshold) / (1 - threshold);
	}

}
