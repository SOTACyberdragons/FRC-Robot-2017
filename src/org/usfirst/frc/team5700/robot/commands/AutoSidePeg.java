package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.Dimensions;
import org.usfirst.frc.team5700.robot.commands.DrivePastDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoSidePeg extends CommandGroup {

    //drive forward 6 feet, minus half of robot length (including bumpers).
	//start from edge of wall, turn to face peg just under 60 deg to compensate for inertia
	//We want to rotate about the center of the robot.
	double firstDistanceIn;
    boolean turnLeft;
    double turnAngleDeg;
    //3.5 feet turn radius
    double turnRadiusIn;
    double driveSpeed;
    
    //drive back 4 feet
  	double driveDistanceIn = 4 * 12;
  	
  	public double toPegDistanceRecord;
	
	//TODO make side enum
    public AutoSidePeg(String side) {
    	
    	firstDistanceIn = Robot.prefs.getDouble("First Distance Inches", 60);
    	turnAngleDeg = Robot.prefs.getDouble("Turn Angle Degrees", 60);
    	turnRadiusIn = Robot.prefs.getDouble("Turn Radius Inches", 20);
    	driveSpeed = Robot.prefs.getDouble("driveSpeed", 0.5);
    	driveDistanceIn = Robot.prefs.getDouble("Drive Back Distance Inches", 4 * 12);
    	
		
		//right: 1; left: -1
		turnLeft = side.equals("left");
		
		addSequential(new DrivePastDistance(firstDistanceIn, driveSpeed, false));
    	
    	//turn angle to face peg
    	addSequential(new TurnRadiusToAngle(turnRadiusIn, turnAngleDeg, driveSpeed, turnLeft));
    	
    	//ram into peg, timeout after 3 seconds
    	addSequential(new GetPegWithVision(false, false), 3);
    	
    	//hang gear while driving back, lift gear intake at end of HangGear command
    	addParallel(new HangGear());
    	addSequential(new DrivePastDistance(driveDistanceIn, -driveSpeed, false), 2);
//    	
//    	//turn back angle
//    	addSequential(new TurnRadiusToAngle(turnRadiusIn, turnAngleDeg, -driveSpeed, turnLeft));
//    	
//    	//return to start
//    	addSequential(new DrivePastDistance(firstDistanceIn, -driveSpeed, true));
    }
}
