/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IndexShootingCommand extends CommandBase {

  private BallQueueCommand ballQueue;
  int ballsShot = 0;

  public IndexShootingCommand() {
    addRequirements(Robot.storage, Robot.shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ballQueue = new BallQueueCommand();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.shooter.isWheelSpunUp()) {
      ballQueue.schedule(false);

    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (ballsShot == 5) {
      return true;
    } else {
      return false;
    }
  }
}
