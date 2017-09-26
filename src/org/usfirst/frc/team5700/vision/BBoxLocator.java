package org.usfirst.frc.team5700.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class BBoxLocator {
	
	NetworkTable table;
	double[] defaultValue = new double[1];
	double[] array = new double[4];
	
	double xTopLeft = 0, 
			yTopLeft = 1, 
			xBottomRight = 2, 
			yBottomRight = 3;
	
	public BBoxLocator(){
	}
	
	private double[] getCoords() {
		for (int i = 0; i < 4; i++) {
			array[i] = table.getNumberArray("detect_float", defaultValue)[i];
		}
		return array;
	}
	
}
