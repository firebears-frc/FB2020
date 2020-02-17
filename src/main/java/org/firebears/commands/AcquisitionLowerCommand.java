package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.firebears.Robot;

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
        Robot.loader.beltForward();
        if (Robot.loader.isJammed()) {
            cancel();
            CommandScheduler.getInstance().schedule(new SpitCommand());
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
