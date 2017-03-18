/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.utils.Side;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive, stop after hitting peg, place gear, drive back, close gear holder.
 */
public class DriveAndPlaceGearSide extends CommandGroup {
	
	
    public  DriveAndPlaceGearSide(Side side) {
    	
    	int direction = 1;
    	switch(side) {
    		case RIGHT: direction = -1;
    		case LEFT: direction = 1;
    	}
    	
    	Preferences prefs = Preferences.getInstance();
		double forwardDistance = prefs.getDouble("Forward Distance", 0);
		double secondForwardDistance = prefs.getDouble("Second Forward Distance", 0);
		double backwardDistance = prefs.getDouble("Backward Distance", 0);
		double angle = prefs.getDouble("Side Gear Turn Angle", 60.0) * direction;
		
		//first, drive to peg and stop after hitting the peg or reaching set distance
    	addSequential(new DriveStraightWithStop(forwardDistance));
    	
    	addSequential(new Turn(angle));
    	
    	//TODO: add final forward
    	addSequential(new DriveStraight(secondForwardDistance));
    	
    	//second, drop the gear holder (TODO: hoping we hit the peg above) 
    	addSequential(new GearDropOnly());
    	
    	//third, drive back a bit
    	addSequential(new DriveStraight(backwardDistance));
    	
    	//fourth, close the gear holder
    	addSequential(new GearHolderUp());
    }

}
