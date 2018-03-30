package org.usfirst.frc.team5700.utils;

public class ReluFilter {
	
	private double threshold;
	private double exp;
	private double denom;
	
	public ReluFilter(double threshold) {
		
		this.threshold = threshold;

	}
	
	public ReluFilter() {

		this.threshold = 0.1;
	}
	
	public double output(double input) {
		
		double magn = Math.abs(input);
		
		return magn < threshold ? 0 : input;
	}

}
