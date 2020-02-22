package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.Robot;
import org.firebears.subsystems.Storage;
import org.firebears.subsystems.Shooter;

public class IndexShootingCommand extends CommandBase {

    private final Storage storage;
    private final Shooter shooter;

    private final BallQueueCommand ballQueue;

    public IndexShootingCommand(Storage storage, Shooter shooter) {
        this.storage = storage;
        this.shooter = shooter;
        ballQueue = new BallQueueCommand(storage);
        addRequirements(storage);
    }

    @Override
    public void initialize() {
        Robot.lights.shoot(true);
    }

    @Override
    public void execute() {
        if (shooter.isWheelSpunUp()) {
            ballQueue.schedule(false);
        }
    }

    @Override
    public boolean isFinished() {
        return storage.getPowerCellCount() == 0;
    }

    @Override
    public void end(boolean interrupted) {
    }
}