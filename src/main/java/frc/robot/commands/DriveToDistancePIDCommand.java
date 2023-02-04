// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Chassis;

public class DriveToDistancePIDCommand extends PIDCommand {
  private static Chassis m_chassis;
  private double m_distance;
  private Timer timer;
  private double m_timeout;

  /** Drive robot forward a distance in inches. */
  public DriveToDistancePIDCommand(double distance, Chassis chassis, double timeout) {
    super(
        // The controller that the command will use
        new PIDController(1, 0, 0),

        // This should return the measurement
        () -> -1 * m_chassis.getEncoderDistance(),
        // This should return the setpoint (can also be a constant)
        distance,
        // This uses the output
        output -> {
          // Use the output here
          m_chassis.arcadeDrive(-1 * MathUtil.clamp(output, 0, 0),0.7);
        });
    m_chassis = chassis;
    m_distance = distance;
    m_timeout = timeout;
    timer = new Timer();
    addRequirements(chassis);
    // Configure additional PID options by calling `getController` here.
  }

  @Override
  public void initialize() {
    m_chassis.resetEncoder();
    timer.reset();
    timer.start();
    SmartDashboard.putBoolean("PIDcmd", true);
  }

  @Override
  public boolean isFinished() {
    System.out.println("Distance from setpoint: " + Math.abs(m_chassis.getEncoderDistance() + m_distance));
    SmartDashboard.putNumber("Distance from", Math.abs(m_chassis.getEncoderDistance() + m_distance));
    SmartDashboard.putBoolean("PID_1", Math.abs(m_chassis.getEncoderDistance() + m_distance) <= 2.0);
    SmartDashboard.putBoolean("PID_2", timer.hasElapsed(m_timeout));
    return Math.abs(m_chassis.getEncoderDistance() + m_distance) <= 2.0 || timer.hasElapsed(m_timeout);
  }
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean("PIDcmd", false);
    SmartDashboard.putBoolean("PIDcmdINT", interrupted);
  }
  
}