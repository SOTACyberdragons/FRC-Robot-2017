package org.usfirst.frc.team5700.utils;

/**
 * DESCRIPTION: <br>
 * Squares the input, preserving sign
 * 
 */

public class SquareFilter {
	
	public static double output(double input) {
		
		return Math.copySign(input * input, input);
	}

}
