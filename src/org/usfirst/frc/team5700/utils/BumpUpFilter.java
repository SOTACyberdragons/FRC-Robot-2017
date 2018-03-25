package org.usfirst.frc.team5700.utils;

public class BumpUpFilter {
	
	private double threshold;
	private double exp;
	
	
	/**
	 * @param exponent (50)
	 */
	public BumpUpFilter(double exp) {
		
		this.exp = exp;
	}
	
	public double output(double input) {
		
		return 1 / (1 + Math.exp(-exp * input)) - 0.5 + input/2;
	}

}
