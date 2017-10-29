package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoMiddlePeg extends CommandGroup {
	
	//drive back 4 feet
	double driveDistanceIn = 4 * 12; 

    public AutoMiddlePeg() {
    	//ram into peg, timeout after 3 seconds
    	addSequential(new GetPegWithVision(true, false), 3);
    	
    	//hang gear while driving back 4 ft, lift gear intake at end of HangGear command
    	addParallel(new HangGear());
    	addSequential(new DrivePastDistance(driveDistanceIn, -0.6, true), 1);
    }
}
