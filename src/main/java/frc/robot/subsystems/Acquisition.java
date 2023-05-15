// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import static frc.robot.Constants.AcquisitionConstants.*;
import static frc.robot.Constants.CONFIG_SET_TIMEOUT_MS;

public class Acquisition extends SubsystemBase {
  private final WPI_TalonSRX starsMotor;
  
  public Acquisition() {
    starsMotor = new WPI_TalonSRX(STARS_MOTOR_ID);
    starsMotor.configPeakCurrentLimit(STARS_PEAK_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    starsMotor.configPeakCurrentDuration(STARS_PEAK_CURRENT_DURATION_MS, CONFIG_SET_TIMEOUT_MS);
    starsMotor.configContinuousCurrentLimit(STARS_CONTINUOUS_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    starsMotor.enableCurrentLimit(true);
    
  }

  public void startAcquire() {
    starsMotor.set(1.0);
  }

  public void endAcquire() {
    starsMotor.set(0.0);
  }

  public void reverseAcquire() {
    starsMotor.set(-1.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
