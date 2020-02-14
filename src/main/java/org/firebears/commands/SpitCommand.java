package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class SpitCommand extends CommandBase {
  
  public SpitCommand() {
    addRequirements(Robot.storage, Robot.acquisition);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.storage.beltReverse();
    Robot.acquisition.starReverse();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
