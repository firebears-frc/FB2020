package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberRight extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    SpeedControllerGroup group;
    private final WPI_TalonSRX rightClimber;
    private final int TIMEOUT_MS = config.getInt("srx.timeout", 30);

    public ClimberRight() {
        int rightClimberCanID = config.getInt("climber.right.canID", 23);
        rightClimber = new WPI_TalonSRX(rightClimberCanID);
        rightClimber.setInverted(false);
        rightClimber.configPeakCurrentLimit(40, TIMEOUT_MS);
        rightClimber.configPeakCurrentDuration(3000, TIMEOUT_MS);
        rightClimber.configContinuousCurrentLimit(20, TIMEOUT_MS);

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