

package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.firebears.Robot;


public class AcquisitionRaiseCommand extends CommandBase {

    public AcquisitionRaiseCommand() {
        addRequirements(Robot.acquisition, Robot.loader);
    }

    @Override
    public void initialize() {
    }

    
    @Override
    public void execute() {
        Robot.loader.beltReverse();
        Robot.acquisition.endAcquire();
        Robot.loader.beltStop();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}