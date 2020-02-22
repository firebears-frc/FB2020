
package org.firebears.commands;

import org.firebears.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberRetractCommand extends CommandBase {
    private final Climber climber;

    public ClimberRetractCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
  
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.retract();
    }
 
    @Override
    public void end(boolean interrupted) {
    }
 
    @Override
    public boolean isFinished() {
        return false;
    }
}