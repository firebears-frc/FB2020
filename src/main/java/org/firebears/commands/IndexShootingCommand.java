






package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IndexShootingCommand extends CommandBase {

  private BallQueueCommand ballQueue;
  int ballsShot = 0;

  public IndexShootingCommand() {
    addRequirements(Robot.storage, Robot.shooter);
  }

  
  @Override
  public void initialize() {
    ballQueue = new BallQueueCommand();
  }

  
  @Override
  public void execute() {
    if (Robot.shooter.isWheelSpunUp()) {
      ballQueue.schedule(false);

    }
  }

  
  @Override
  public void end(boolean interrupted) {
  }

  
  @Override
  public boolean isFinished() {
    if (ballsShot == 5) {
      return true;
    } else {
      return false;
    }
  }
}
