package org.usfirst.frc.team5700.utils;

public class BumpUpFilter {
	
	private double threshold;
	private double exp;
	private double target;
	
	
	/**
	 * @param exp how steeply the function will move (use 100)
	 * @param threshold where you want joystick to start responding (use 0.1)
	 * @param to what input you should jump at threshold (use 0.3)
	 */
	public BumpUpFilter(double exp, double threshold, double target) {
		
		this.exp = exp;
		this.threshold = threshold;
		this.target = target;
	}
	
	public BumpUpFilter() {
		
		this.exp = 100;
		this.threshold = 0.1;
		this.target = 0.3;
	}
	
	public double output(double input) {
		
		double sign = Math.signum(input);
		double magnitude = Math.abs(input);
		
		//https://www.desmos.com/calculator/uq5ngfl0kl
		
		return sign * (target + (1 - target) * magnitude) / 
				(1 + Math.exp(-exp * (magnitude - threshold)));
	}

}
