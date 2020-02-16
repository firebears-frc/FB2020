package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberLeft extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    private final WPI_TalonSRX leftClimber;
    private final int TIMEOUT_MS = config.getInt("srx.timeout", 30);

    public ClimberLeft() {
        int leftClimberCanID = config.getInt("climber.left.canID", 22);
        leftClimber = new WPI_TalonSRX(leftClimberCanID);
        leftClimber.setInverted(false);
        leftClimber.configPeakCurrentLimit(40, TIMEOUT_MS);
        leftClimber.configPeakCurrentDuration(3000, TIMEOUT_MS);
        leftClimber.configContinuousCurrentLimit(20, TIMEOUT_MS);

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