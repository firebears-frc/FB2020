
package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberUpRightCommand extends CommandBase {
 
  public ClimberUpRightCommand() {
   
    addRequirements(Robot.climberRight);
  }

  
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    Robot.climberRight.rightClimberUp();
  }

 
  @Override
  public void end(boolean interrupted) {
  }

 
  @Override
  public boolean isFinished() {
    return false;
  }
}
