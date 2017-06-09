package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class NetworkTableTest extends Command {
	double a = 0;
	double b = 0;
	NetworkTable table;
    public NetworkTableTest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	table = NetworkTable.getTable("GRIP/grip");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Timer.delay(0.25);
    	a += 0.1;
    	b += 0.1;
		Robot.networkTable.putNumber("a", a);
		Robot.networkTable.putNumber("b", b);
		System.out.println("Is centerX there? " + table.containsKey("centerX"));
		System.out.println("All table keys: " + table.getKeys());
		System.out.println("centerX" + table.getNumberArray("centerX", new double[]{0})[0]);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
