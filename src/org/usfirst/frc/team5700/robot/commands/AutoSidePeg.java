package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.commands.DrivePastDistance;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSidePeg extends CommandGroup {

    //drive forward 6 feet, minus half of robot length (including bumpers).
	//start from edge of wall, turn to face peg just under 60 deg to compensate for inertia
	//We want to rotate about the center of the robot.
	double firstDistanceIn;
    boolean turnLeft;
    double turnAngleDeg;
    double turnRadiusIn;
    double driveSpeed;
    
    //drive back 4 feet
  	double driveDistanceIn;
  	
  	public double toPegDistanceRecord;
	
    public AutoSidePeg(String side) {
    	
    	Preferences prefs = Preferences.getInstance();
    	
    	firstDistanceIn = prefs.getDouble("SidePeg First Distance in", 55);
    	turnAngleDeg = prefs.getDouble("SidePeg Turn Angle deg", 44);
    	turnRadiusIn = prefs.getDouble("SidePeg Radius in", 20);
    	driveSpeed = prefs.getDouble("SidePeg driveSpeed", 0.5);
    	driveDistanceIn = prefs.getDouble("SidePeg Drive Back in", 4 * 12);
		
	//right: 1; left: -1
	turnLeft = side.equals("left");
		
	addSequential(new DrivePastDistance(firstDistanceIn, driveSpeed, false));
    	
    	//turn angle to face peg
    	addSequential(new TurnRadiusPastAngle(turnRadiusIn, turnAngleDeg, driveSpeed, turnLeft, false));
    	
    	//ram into peg, timeout after 3 seconds
    	addSequential(new GetPegWithVision(false, false), 3);
    	
    	//hang gear while driving back, lift gear intake at end of HangGear command
    	addParallel(new AutoHangGear());
    	addSequential(new DrivePastDistance(driveDistanceIn, -driveSpeed, false), 2);
    }
}
