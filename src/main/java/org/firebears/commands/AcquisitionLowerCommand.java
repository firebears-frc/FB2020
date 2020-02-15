package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.firebears.Robot;

/**
 *
 */
public class AcquisitionLowerCommand extends CommandBase {

    public AcquisitionLowerCommand() {
        addRequirements(Robot.acquisition, Robot.loader);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Robot.acquisition.startAcquire();
        Robot.loader.transfer();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
