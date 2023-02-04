// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class DriveToPositionCommand extends CommandBase {
  /** Creates a new DriveToPositionCommand. */
  private double distance;
  private Chassis m_chassis;

  public DriveToPositionCommand(double d, Chassis c) {
    distance = d;
    m_chassis = c;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_chassis);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_chassis.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (distance > 0) {
      m_chassis.arcadeDrive(-0.7, 0);
    } else {
      m_chassis.arcadeDrive(0.7, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_chassis.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (distance > 0 && m_chassis.getEncoderDistance() > distance) {
      return true;
    } else if (distance < 0 && m_chassis.getEncoderDistance() < distance) {
      return true;
    }
    return false;
  }
}