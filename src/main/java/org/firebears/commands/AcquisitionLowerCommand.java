package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.firebears.subsystems.Acquisition;
import org.firebears.subsystems.Loader;
import org.firebears.subsystems.Storage;

public class AcquisitionLowerCommand extends CommandBase {

    private final Acquisition acquisition;
    private final Loader loader;
    private final Storage storage;

    public AcquisitionLowerCommand(Acquisition acquisition,
        Loader loader, Storage storage)
    {
        this.acquisition = acquisition;
        this.loader = loader;
        this.storage = storage;
        addRequirements(acquisition, loader);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (storage.isBackedUp()) {
            acquisition.pause();
            loader.beltStop();
        } else {
            acquisition.startAcquire();
            loader.beltForward();
        }
        if (loader.isJammed()) {
            cancel();
            CommandScheduler.getInstance().schedule(
                new SpitCommand(acquisition, loader));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
