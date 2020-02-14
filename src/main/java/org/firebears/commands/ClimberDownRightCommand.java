
package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberDownRightCommand extends CommandBase {
 
  public ClimberDownRightCommand() {
   
    addRequirements(Robot.climberRight);
  }

  
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    Robot.climberRight.rightClimberDown();
  }

 
  @Override
  public void end(boolean interrupted) {
  }

 
  @Override
  public boolean isFinished() {
    return false;
  }
}
