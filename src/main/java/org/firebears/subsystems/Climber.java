package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    private final WPI_TalonSRX srx;

    public Climber(String configCANID, int defCANID) {
        int timeoutMs = config.getInt("srx.timeout", 30);
        int peakCurrentLimit = config.getInt("climber.peakCurrentLimit", 40);
        int peakCurrentDuration = config.getInt("climber.peakCurrentDuration",
            3000);
        int continuousCurrentLimit = config.getInt(
           "climber.continuousCurrentLimit", 20);
        int canID = config.getInt(configCANID, defCANID);
        srx = new WPI_TalonSRX(canID);
        srx.configForwardSoftLimitEnable(false, timeoutMs);
        srx.configReverseSoftLimitEnable(false, timeoutMs);
        srx.setInverted(false);
        srx.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        srx.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        srx.configContinuousCurrentLimit(continuousCurrentLimit,
            timeoutMs);

        addChild(configCANID, srx);
    }

    @Override
    public void periodic() {
    }

    public void extend() {
        srx.set(ControlMode.PercentOutput, 0.5);
    }

    public void retract() {
        srx.set(ControlMode.PercentOutput, -0.5);
    }

    public void stop() {
        srx.set(ControlMode.PercentOutput, 0);
    }
}