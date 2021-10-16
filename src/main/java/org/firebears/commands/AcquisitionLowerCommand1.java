// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;


import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.firebears.subsystems.Acquisition;
import org.firebears.subsystems.Loader;
import org.firebears.subsystems.Storage;

public class AcquisitionLowerCommand1 extends CommandBase {
  /** Creates a new AcquisitionLowerCommand1. */
    private final Acquisition acquisition;
    private final Loader loader;
    private final Storage storage;

    public AcquisitionLowerCommand1(Acquisition acquisition,
        Loader loader, Storage storage)
    {
        this.acquisition = acquisition;
        this.loader = loader;
        this.storage = storage;
        addRequirements(acquisition, loader);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    loader.beltForward();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    acquisition.startAcquire();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  //  acquisition.pauseDown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return acquisition.isDown();
  }
}
