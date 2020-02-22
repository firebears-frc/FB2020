package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Acquisition;
import org.firebears.subsystems.Loader;

public class SpitCommand extends CommandBase {

    private final Acquisition acquisition;
    private final Loader loader;

    // Remaining time in 20 ms ticks
    private int remaining = 100;

    public SpitCommand(Acquisition acquisition, Loader loader) {
        this.acquisition = acquisition;
        this.loader = loader;
        addRequirements(acquisition, loader);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        acquisition.starReverse();
        loader.beltReverse();
        remaining--;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return remaining <= 0;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }
}