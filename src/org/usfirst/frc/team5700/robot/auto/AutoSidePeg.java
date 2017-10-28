package org.usfirst.frc.team5700.robot.auto;

import org.usfirst.frc.team5700.robot.RobotDimensions;
import org.usfirst.frc.team5700.robot.auto.DrivePastDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoSidePeg extends CommandGroup {

    //drive forward 6 feet, minus half of robot length (including bumpers).
	//start from edge of wall, turn to face peg just under 60 deg to compensate for inertia
	//We want to rotate about the center of the robot.
	double firstDistanceIn = 6 * 12 - RobotDimensions.LENGTH_IN;
    double turnAngleDeg = 55;
    //3.5 feet turn radius
    double turnRadiusIn = 3.5 * 12;
    double driveSpeed = 0.8;
    
    //drive back 4 feet
  	double driveDistanceIn = -4 * 12; 
	
	//TODO make side enum
    public AutoSidePeg(String side) {
		
		//right: 1; left: -1
		turnAngleDeg *= side.equals("left") ? -1 : 1;
		
    	addSequential(new DrivePastDistance(firstDistanceIn, driveSpeed));
    	
    	//turn angle to face peg
    	addSequential(new TurnRadiusToAngle(turnAngleDeg, turnRadiusIn, driveSpeed));
    	
    	//ram into peg, timeout after 3 seconds
    	addSequential(new PegWithVision(false), 3);
    	
    	//hang gear while driving back, lift gear intake at end of HangGear command
    	addParallel(new HangGear());
    	addSequential(new DriveStraightWithStop(driveDistanceIn));
    }
}
