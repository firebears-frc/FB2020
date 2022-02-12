// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class DriveCommand extends CommandBase {
  XboxController m_xboxController;
  Chassis m_chassis;
  /** Creates a new stupid. */
  public DriveCommand(Chassis chassis, XboxController xboxController) {
    m_xboxController = xboxController;
    m_chassis = chassis; 
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_chassis);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize(){}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    m_chassis.drive(m_xboxController.getLeftX(), m_xboxController.getLeftY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}