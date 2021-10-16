package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Acquisition;
import org.firebears.subsystems.Loader;

public class AcquisitionRaiseCommand1 extends CommandBase {

    private final Acquisition acquisition;
    private final Loader loader;



    public AcquisitionRaiseCommand1(Acquisition acquisition, Loader loader)
    {
        this.acquisition = acquisition;
        this.loader = loader;
        addRequirements(acquisition, loader);
        
    }

    @Override
    public void initialize() {
        loader.beltStop();
    }
    
    @Override
    public void execute() {
        acquisition.endAcquire();
    }
    
    @Override
    public boolean isFinished() {
        return acquisition.isUp();
    }

    @Override
    public void end(boolean interrupted) {
        acquisition.pause();
    }

    
}