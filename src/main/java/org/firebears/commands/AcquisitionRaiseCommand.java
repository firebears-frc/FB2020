package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.Robot;

public class AcquisitionRaiseCommand extends CommandBase {

    // Remaining time in 20 ms ticks
    private int remaining = 100;

    public AcquisitionRaiseCommand() {
        addRequirements(Robot.acquisition, Robot.loader);
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void execute() {
        remaining--;
        Robot.acquisition.endAcquire();
        if (remaining > 0)
            Robot.loader.beltReverse();
        else
            Robot.loader.beltStop();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}