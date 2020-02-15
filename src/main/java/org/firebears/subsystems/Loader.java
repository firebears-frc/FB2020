package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Loader extends SubsystemBase {
    private final Preferences config = Preferences.getInstance();

    private final WPI_TalonSRX leftBeltMotor;
    private final WPI_TalonSRX rightBeltMotor;
    private final SpeedControllerGroup belts;
    private final DigitalInput loadEye;

    public Loader() {
        leftBeltMotor = new WPI_TalonSRX(config.getInt("loader.leftBeltMotor.canID", 20));
        rightBeltMotor = new WPI_TalonSRX(config.getInt("loader.rightBeltMotor.canID", 21));
        belts = new SpeedControllerGroup(leftBeltMotor, rightBeltMotor);
        loadEye = new DigitalInput(config.getInt("loader.eye.dio", 5));
    }

    @Override
    public void periodic() {

    }

    public void transfer() {
        belts.set(0.5);
    }

    public void beltStop() {
        belts.set(0);
    }

    public void beltReverse() {
        belts.set(-0.5);
    }
}