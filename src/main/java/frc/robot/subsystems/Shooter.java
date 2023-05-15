// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.CONFIG_SET_TIMEOUT_MS;;

public class Shooter extends SubsystemBase {
  private WPI_TalonSRX shooterMotor;
  
  public Shooter() {
    shooterMotor = new WPI_TalonSRX(SHOOTER_MOTOR_ID);
    shooterMotor.setInverted(true);
    // Configure nominal / peak outputs
    shooterMotor.configNominalOutputForward(0, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.configNominalOutputReverse(0, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.configPeakOutputForward(1, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.configPeakOutputReverse(1, CONFIG_SET_TIMEOUT_MS);
    // Configure limits
    shooterMotor.configPeakCurrentLimit(PEAK_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.configPeakCurrentDuration(PEAK_CURRENT_DURATION_MS, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    shooterMotor.enableCurrentLimit(true);
  }

  public void shootSpeed() {
    shooterMotor.set(SHOOTER_SHOOT_SPEED);
  }
  public void idleSpeed() {
    shooterMotor.set(SHOOTER_IDLE_SPEED);
  }
  public void stop() {
    shooterMotor.set(0);
  }

  @Override
  public void periodic() {
  }
}
