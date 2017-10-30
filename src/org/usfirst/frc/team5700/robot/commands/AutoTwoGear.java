package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoTwoGear extends CommandGroup {

    private boolean turnLeft;

	public AutoTwoGear(String side) {
        /**
         * Plan:
         * 
         * 1. Middle Peg (drive with vision, hang, drive back 56 inches)
         * 		1.1 Switch Model When Peg Hits
         * 2. Blind turn to preset best guess angle in place (no vision, no PID, record angle)
         * 3. Vision, turn in place until aligned with gear (record angle)
         * 4. Drive, pick up gear with vision
         * 		4.1. Switch model to peg using gear size larger than x
         * 		4.2. Optional, use current spike to detect intaken gear
         * 5. Drive back recorded distance at same heading
         * 6. Turn back to angle recorded angle (Vision and blind)
         * 7. Middle Peg with vision
         */
    	
    	turnLeft = side.equals("left");
    	
    	//PART 1
    	//ram into peg, timeout after 3 seconds
    	addSequential(new GetPegWithVision(true, false), 3);
    	
    	//hang gear while driving back 4 ft, lift gear intake at end of HangGear command
    	addParallel(new HangGear());
    	addSequential(new DrivePastDistance(44, -0.6, true), 2);
    	
    	
    	//PART 2
    	//No PID turn angle, make sure to record
    	addSequential(new TurnRadiusToAngle(0, 90, 0.32, !turnLeft));
    	
    	//PART 3
    	addSequential(new TurnToGearWithVision());
    	
    	//PART 4
    	addSequential(new GetGearWithVision(true));
    	
    	//PART 5
    	addSequential(new DrivePastDistance(-0.6, true));
    	
    	//PART 6
    	addSequential(new TurnRadiusToAngle(0, 0.4, turnLeft));
    	
    	//PART 7
    	addSequential(new GetPegWithVision(true, true));
    	
    }
}
