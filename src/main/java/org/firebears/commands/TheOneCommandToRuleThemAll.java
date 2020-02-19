/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TheOneCommandToRuleThemAll extends CommandBase {
  /**
   * Creates a new TheOneCommandToRuleThemAll.
   */
  private final BallQueueCommand queue;
  public TheOneCommandToRuleThemAll() {
    
    addRequirements(Robot.storage);
    queue = new BallQueueCommand();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.storage.needsIndexing()){
     queue.schedule(false);
    }
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
