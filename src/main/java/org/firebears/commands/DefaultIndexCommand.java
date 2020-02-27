package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Storage;

public class DefaultIndexCommand extends CommandBase {

    private final Storage storage;

    private final BallQueueCommand queue;

    public DefaultIndexCommand(Storage storage) {
        this.storage = storage;
        addRequirements(storage);
        queue = new BallQueueCommand(storage);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (storage.needsIndexing()){
            queue.schedule(false);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}