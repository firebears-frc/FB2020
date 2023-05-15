// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;

public class StorageAdvanceCommand extends CommandBase {
  
  private Storage storage;
  private boolean hasBeenUnaligned;
  public StorageAdvanceCommand(Storage s) {
    // Use addRequirements() here to declare subsystem dependencies.
    storage = s;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    hasBeenUnaligned = false;
    storage.move();
  }

  // Called every time the scheduler runs while the command is scheduled. 
  public void execute() {
    if (!storage.getAligned()) {
      hasBeenUnaligned = true;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return storage.getAligned() && hasBeenUnaligned;
  }
}
