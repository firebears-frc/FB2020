// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Loader;

public class AcquisitionStart extends InstantCommand {
  private Acquisition m_acquisition;
  private Loader m_loader;
  public AcquisitionStart(Acquisition a, Loader l) {
    m_acquisition = a;
    m_loader = l;
    addRequirements(m_acquisition, m_loader);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_acquisition.startAcquire();
    m_loader.intake();
  }
}
