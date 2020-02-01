package org.firebears.commands;

import org.firebears.Robot;



import edu.wpi.first.wpilibj2.command.CommandBase;


public class BallQueueCommand extends CommandBase {

  boolean aligned;

  public BallQueueCommand() {

    addRequirements(Robot.storage);
  }

  @Override
  public void initialize() {
    Robot.storage.move();
  }

  @Override
  public void execute() {
    aligned = Robot.storage.getPositionSensor();
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    if (aligned = true){
      Robot.storage.stop();
      return true;
    }else{
      return false;
    }
  }
}
