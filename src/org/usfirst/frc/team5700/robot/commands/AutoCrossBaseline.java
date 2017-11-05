package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Dimensions;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCrossBaseline extends CommandGroup {

	double distance = Dimensions.DISTANCE_TO_BASELINE;
	
    public AutoCrossBaseline() {
    		addSequential(new DrivePastDistance(distance, 0.7, true), 3);
    }
}
