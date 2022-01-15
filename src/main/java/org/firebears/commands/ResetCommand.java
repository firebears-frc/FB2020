package org.firebears.commands;

import org.firebears.subsystems.Storage;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ResetCommand extends CommandBase {
    private final Storage storage;
    private final boolean pos5Filled;

    public ResetCommand(Storage storage) {
        this.storage = storage;
        addRequirements(storage);

        pos5Filled = storage.isIndexFull();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        storage.move();
    }

    @Override
    public boolean isFinished() {
        boolean aligned = storage.getPositionSensor();
        if (aligned||pos5Filled) {
            storage.stop();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
    }
}
