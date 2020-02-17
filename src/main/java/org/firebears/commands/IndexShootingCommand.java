package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IndexShootingCommand extends CommandBase {

    private BallQueueCommand ballQueue;

    public IndexShootingCommand() {
        addRequirements(Robot.storage, Robot.shooter);
    }

    @Override
    public void initialize() {
        ballQueue = new BallQueueCommand();
        Robot.lights.shoot(true);
    }

    @Override
    public void execute() {
        if (Robot.shooter.isWheelSpunUp()) {
            ballQueue.schedule(false);
        }
    }

    @Override
    public boolean isFinished() {
        return Robot.storage.getPowerCellCount() == 0;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
