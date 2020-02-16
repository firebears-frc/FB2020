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
        int timeoutMs = config.getInt("srx.timeout", 30);
        int leftBeltCanID = config.getInt("loader.leftBeltMotor.canID", 20);
        int rightBeltCanID = config.getInt("loader.rightBeltMotor.canID", 21);
        int eyeDio = config.getInt("loader.eye.dio", 5);
        int peakCurrentLimit = config.getInt("loader.peakCurrentLimit", 15);
        int peakCurrentDuration = config.getInt("loader.peakCurrentDuration", 5000);
        int continuousCurrentLimit = config.getInt("loader.continuousCurrentLimit", 10);

        leftBeltMotor = new WPI_TalonSRX(leftBeltCanID);
        leftBeltMotor.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        leftBeltMotor.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        leftBeltMotor.configContinuousCurrentLimit(continuousCurrentLimit, timeoutMs);
        rightBeltMotor = new WPI_TalonSRX(rightBeltCanID);
        rightBeltMotor.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        rightBeltMotor.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        rightBeltMotor.configContinuousCurrentLimit(continuousCurrentLimit, timeoutMs);
        belts = new SpeedControllerGroup(leftBeltMotor, rightBeltMotor);
        loadEye = new DigitalInput(eyeDio);
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