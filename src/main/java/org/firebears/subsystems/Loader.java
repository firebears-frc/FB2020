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
   // private final SpeedControllerGroup belts;
    private final DigitalInput loadEye;
    private int eyeDuration;

    /** Duration in 20 ms ticks */
    static private final int MAX_DURATION = 100;

    public Loader() {
        int timeoutMs = config.getInt("srx.timeout", 30);
        int leftBeltCanID = config.getInt("loader.leftBeltMotor.canID", 20);
        int rightBeltCanID = config.getInt("loader.rightBeltMotor.canID", 21);
        int eyeDio = config.getInt("loader.eye.dio", 9);
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
        rightBeltMotor.setInverted(true);

       // belts = new SpeedControllerGroup(leftBeltMotor, rightBeltMotor);
        loadEye = new DigitalInput(eyeDio);
        //addChild("Belts", belts);
    }

    @Override
    public void periodic() {
        if (loadEye.get()) {
            eyeDuration++;
        } else {
            eyeDuration = 0;
        }
    }

    public void beltForward() {
        rightBeltMotor.set(-1.0);
        leftBeltMotor.set(-1.0);
    }

    public void beltStop() {
        rightBeltMotor.set(0);
        leftBeltMotor.set(0);
    }

    public void beltReverse() {
        rightBeltMotor.set(1.0);
        leftBeltMotor.set(1.0);
    }

    public boolean isJammed() {
        return false;
//        return eyeDuration > MAX_DURATION;
    }
}
