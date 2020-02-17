package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberLeft extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    private final WPI_TalonSRX leftClimber;

    public ClimberLeft() {
        int timeoutMs = config.getInt("srx.timeout", 30);
        int peakCurrentLimit = config.getInt("climber.peakCurrentLimit", 40);
        int peakCurrentDuration = config.getInt("climber.peakCurrentDuration",
            3000);
        int continuousCurrentLimit = config.getInt(
            "climber.continuousCurrentLimit", 20);
        int leftClimberCanID = config.getInt("climber.left.canID", 22);
        leftClimber = new WPI_TalonSRX(leftClimberCanID);
        leftClimber.setInverted(false);
        leftClimber.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        leftClimber.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        leftClimber.configContinuousCurrentLimit(continuousCurrentLimit,
            timeoutMs);

        addChild("Left Climber", leftClimber);
    }

    @Override
    public void periodic() {

    }

    public void leftClimberUp() {
        leftClimber.set(ControlMode.PercentOutput, 1);
    }

    public void leftClimberDown() {
        leftClimber.set(ControlMode.PercentOutput, -1);
    }
}
