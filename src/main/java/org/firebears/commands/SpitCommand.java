package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpitCommand extends CommandBase {

    // Remaining time in 20 ms ticks
    private int remaining = 100;

    public SpitCommand() {
        addRequirements(Robot.loader, Robot.storage);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.loader.beltReverse();
        Robot.acquisition.starReverse();
        remaining--;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return remaining <= 0;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }
}