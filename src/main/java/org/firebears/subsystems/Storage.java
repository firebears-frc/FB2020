package org.firebears.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Storage extends SubsystemBase {
    private final Preferences config = Preferences.getInstance();

    private final CANSparkMax indexMotor;

    private final DigitalInput positionSensor;
    private final DigitalInput eye1;
    private final DigitalInput eye2;
    private final DigitalInput eye3;
    private final DigitalInput eye4;
    private final DigitalInput eye5;

    public Storage() {
        int indexMotorCanID = config.getInt("storage.indexMotor.canID", 10);
        indexMotor = new CANSparkMax(indexMotorCanID, MotorType.kBrushless);
        indexMotor.setInverted(false);

        positionSensor = new DigitalInput(config.getInt("storage.position.dio", 4));
        eye1 = new DigitalInput(config.getInt("storage.eye1.dio", 6));
        eye2 = new DigitalInput(config.getInt("storage.eye2.dio", 7));
        eye3 = new DigitalInput(config.getInt("storage.eye3.dio", 8));
        eye4 = new DigitalInput(config.getInt("storage.eye4.dio", 9));
        eye5 = new DigitalInput(config.getInt("storage.eye5.dio", 10));
    }

    @Override
    public void periodic() {

    }

    public void move() {
        indexMotor.set(0.1);
    }

    public void stop() {
        indexMotor.set(0.0);
    }

    public boolean getPositionSensor() {
        return positionSensor.get();
    }
}