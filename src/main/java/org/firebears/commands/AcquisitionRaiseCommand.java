package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Acquisition;
import org.firebears.subsystems.Loader;

public class AcquisitionRaiseCommand extends CommandBase {

    private final Acquisition acquisition;

    private final Loader loader;

    // Remaining time in 20 ms ticks
    private int remaining = 100;

    public AcquisitionRaiseCommand(Acquisition acquisition,
        Loader loader)
    {
        this.acquisition = acquisition;
        this.loader = loader;
        addRequirements(acquisition, loader);
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void execute() {
        remaining--;
        acquisition.endAcquire();
        if (remaining > 0)
            loader.beltReverse();
        else
            loader.beltStop();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}