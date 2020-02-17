//moves index 72 degrees
package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class BallQueueCommand extends CommandBase {

    public BallQueueCommand() {
        addRequirements(Robot.storage);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Robot.storage.move();
    }

    @Override
    public boolean isFinished() {
        boolean aligned = Robot.storage.getPositionSensor();
        if (aligned) {
            Robot.storage.stop();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
    }
}
