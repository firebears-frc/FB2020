// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;



import static frc.robot.Constants.StorageConstants.*;


public class Storage extends SubsystemBase {

  private final CANSparkMax indexMotor;

  private final DigitalInput alignedSesnor;

  public Storage() {
    indexMotor = new CANSparkMax(INDEX_MOTOR_ID, MotorType.kBrushless);
    indexMotor.setSmartCurrentLimit(20,10,500);

    alignedSesnor = new DigitalInput(ALIGNED_SENSOR_CHANNEL);
    
  }

  public void run() {
    indexMotor.set(0.4);
  }
  public void reverse() {
    indexMotor.set(-0.4);
  }
  public void stop() {
    indexMotor.set(0.0);
  }

  public boolean getAligned() {
    return alignedSesnor.get();
  }


  @Override
  public void periodic() {

  }
}
