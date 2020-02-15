/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands;

import java.lang.module.ModuleDescriptor.Requires;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class CelebrateCommand extends CommandBase {
  /**
   * Creates a new CelebrateCommand.
   */
  public CelebrateCommand() {
    addRequirements(Robot.lights);
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.lights.celebrate(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
