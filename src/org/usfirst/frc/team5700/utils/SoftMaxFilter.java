package org.usfirst.frc.team5700.utils;

public class SoftMaxFilter {
	
	private double threshold;
	private double exp;
	private double denom;
	
	public SoftMaxFilter(double exp, double threshold, double denom) {
		
		this.exp = exp;
		this.threshold = threshold;
		this.denom = denom;
	}
	
	public SoftMaxFilter() {
		
		this.exp = 200;
		this.threshold = 0.1;
		this.denom = 180;
	}
	
	public double output(double input) {
		
		double sign = Math.signum(input);
		double magnitude = Math.abs(input);
		
		//https://www.desmos.com/calculator/isofr6swzr
		//ln(1+exp(200x-20))/180
		
		return sign * Math.log(1 + Math.exp( exp * (magnitude - threshold))) / denom;
	}

}
