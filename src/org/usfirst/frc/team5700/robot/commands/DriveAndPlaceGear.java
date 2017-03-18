/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Drive, stop after hitting peg, place gear, drive back, close gear holder.
 */
public class DriveAndPlaceGear extends CommandGroup {
	
    public  DriveAndPlaceGear() {
    	Preferences prefs = Preferences.getInstance();
		double forwardDistance = prefs.getDouble("ForwardDistance", 0);
		double backwardDistance = prefs.getDouble("BackwardDistance", 0);
		
		//first, drive to peg and stop after hitting the peg or reaching set distance
    	addSequential(new DriveStraightWithStop(forwardDistance));
    	
    	//second, drop the gear holder (TODO: hoping we hit the peg above) 
    	addSequential(new GearDropOnly());
    	
    	//third, drive back a bit
    	addSequential(new DriveStraight(backwardDistance));
    	
    	//fourth, close the gear holder
    	addSequential(new GearHolderUp());
    }

}
