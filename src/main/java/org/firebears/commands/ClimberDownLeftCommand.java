
package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberDownLeftCommand extends CommandBase {
 
  public ClimberDownLeftCommand() {
    addRequirements(Robot.climberLeft);
  }

  
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    Robot.climberLeft.leftClimberDown();
  }

 
  @Override
  public void end(boolean interrupted) {
  }

 
  @Override
  public boolean isFinished() {
    return false;
  }
}
