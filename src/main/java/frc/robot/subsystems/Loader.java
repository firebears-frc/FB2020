// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import static frc.robot.Constants.LoaderConstants.*;
import static frc.robot.Constants.CONFIG_SET_TIMEOUT_MS;;

public class Loader extends SubsystemBase {
  private final WPI_TalonSRX leftBeltMotor;
  private final WPI_TalonSRX rightBeltMotor;
  
  public Loader() {
    leftBeltMotor = new WPI_TalonSRX(LEFT_BELT_MOTOR_ID);
    leftBeltMotor.configPeakCurrentLimit(PEAK_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    leftBeltMotor.configPeakCurrentDuration(PEAK_CURRENT_DURATION_MS, CONFIG_SET_TIMEOUT_MS);
    leftBeltMotor.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    leftBeltMotor.enableCurrentLimit(true);

    rightBeltMotor = new WPI_TalonSRX(RIGHT_BELT_MOTOR_ID);
    rightBeltMotor.configPeakCurrentLimit(PEAK_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    rightBeltMotor.configPeakCurrentDuration(PEAK_CURRENT_DURATION_MS, CONFIG_SET_TIMEOUT_MS);
    rightBeltMotor.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT, CONFIG_SET_TIMEOUT_MS);
    rightBeltMotor.enableCurrentLimit(true);
    rightBeltMotor.setInverted(true);
    

  }

  public void intake() {
    rightBeltMotor.set(-1.0);
    leftBeltMotor.set(-1.0);
  }

  public void reverse() {
    rightBeltMotor.set(1.0);
    leftBeltMotor.set(1.0);
  }

  public void stop() {
    rightBeltMotor.set(0.0);
    leftBeltMotor.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
