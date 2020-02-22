package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Climber;

public class ClimberExtendCommand extends CommandBase {
    private final Climber climber;
 
    public ClimberExtendCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.extend();
    }
 
    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }
 
    @Override
    public boolean isFinished() {
        return false;
    }
}