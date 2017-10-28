package org.usfirst.frc.team5700.robot.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCrossBaseline extends CommandGroup {

	//distance to baseline: 93 in
	double distance = 100;
	
    public AutoCrossBaseline() {
    	addSequential(new DriveStraightWithStop(distance));
    }
}
