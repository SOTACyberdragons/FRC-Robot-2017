package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ReplayWithCommands extends CommandGroup {

    public ReplayWithCommands() {
    	addParallel(new DriveReplay());
    	addParallel(new ClimbUp(1, true));
    }
}
