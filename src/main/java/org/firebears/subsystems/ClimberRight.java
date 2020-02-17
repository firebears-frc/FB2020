package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberRight extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    private final WPI_TalonSRX rightClimber;

    public ClimberRight() {
        int timeoutMs = config.getInt("srx.timeout", 30);
        int peakCurrentLimit = config.getInt("climber.peakCurrentLimit", 40);
        int peakCurrentDuration = config.getInt("climber.peakCurrentDuration",
            3000);
        int continuousCurrentLimit = config.getInt(
            "climber.continuousCurrentLimit", 20);
        int rightClimberCanID = config.getInt("climber.right.canID", 23);
        rightClimber = new WPI_TalonSRX(rightClimberCanID);
        rightClimber.setInverted(false);
        rightClimber.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        rightClimber.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        rightClimber.configContinuousCurrentLimit(continuousCurrentLimit,
            timeoutMs);

        addChild("Right Climber", rightClimber);
    }

    @Override
    public void periodic() {

    }

    public void rightClimberUp() {
        rightClimber.set(ControlMode.PercentOutput, 1);
    }

    public void rightClimberDown() {
        rightClimber.set(ControlMode.PercentOutput, -1);
    }
}
