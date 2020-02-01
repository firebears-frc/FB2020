

package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Storage extends SubsystemBase {
  private CANSparkMax indexMotor;
  private WPI_TalonSRX belt1Motor;
  private WPI_TalonSRX belt2Motor;
  private SpeedControllerGroup belts;

  private DigitalInput positionSensor;
  private DigitalInput loadEye; 
  private DigitalInput eye1;
  private DigitalInput eye2;
  private DigitalInput eye3;
  private DigitalInput eye4;
  private DigitalInput eye5;
  
  

  final Preferences config = Preferences.getInstance();

  public Storage() {
    belt1Motor = new WPI_TalonSRX(config.getInt("storage.belt1Motor.canID", 16));  // PDP channel 0
    belt2Motor = new WPI_TalonSRX(config.getInt("storage.belt2Motor.canID", 15));  // PDP channel 1
    belts = new SpeedControllerGroup(belt1Motor, belt2Motor);

    int indexMotorCanID = config.getInt("storage.indexMotor.canID", 14);
        indexMotor = new CANSparkMax(indexMotorCanID, MotorType.kBrushless);
        indexMotor.setInverted(false);

    positionSensor = new DigitalInput(config.getInt("storage.position.dio", 4));
    loadEye = new DigitalInput(config.getInt("storage.loadEye.dio", 5));
    eye1 = new DigitalInput(config.getInt("storage.eye1.dio", 6));
    eye2 = new DigitalInput(config.getInt("storage.eye2.dio", 7));
    eye3 = new DigitalInput(config.getInt("storage.eye3.dio", 8));
    eye4 = new DigitalInput(config.getInt("storage.eye4.dio", 9));
    eye5 = new DigitalInput(config.getInt("storage.eye5.dio", 10));

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void transfer(){
    belts.set(0.1);
  }

  public void move(){
    indexMotor.set(0.1);
  }

  public void stop(){
    indexMotor.set(0.0);
  }
  public boolean getPositionSensor(){
     return positionSensor.get();
  }

}
