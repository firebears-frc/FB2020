package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ResetCommand extends CommandBase {

    public ResetCommand() {
        addRequirements(Robot.storage);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Robot.storage.reverse();
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
