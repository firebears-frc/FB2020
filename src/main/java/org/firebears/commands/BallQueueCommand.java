//moves index 72 degrees
package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Storage;

public class BallQueueCommand extends CommandBase {

    private final Storage storage;
    private boolean hasBeenUnaligned;

    public BallQueueCommand(Storage storage) {
        this.storage = storage;
        addRequirements(storage);
    }

    @Override
    public void initialize() {
        hasBeenUnaligned = false;
    }

    @Override
    public void execute() {
        if (storage.getPositionSensor() == false){
            hasBeenUnaligned = true;
        }
        storage.move();
    }

    @Override
    public boolean isFinished() {
        boolean aligned = storage.getPositionSensor();
        if (aligned && hasBeenUnaligned) {
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