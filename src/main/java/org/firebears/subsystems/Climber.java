package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Climber extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    SpeedControllerGroup group;
    private final WPI_TalonSRX rightClimber;
    private final WPI_TalonSRX leftClimber;
        public Climber() {
        int rightClimberCanID = config.getInt("climber.rightClimber.canID", 8);
        rightClimber = new WPI_TalonSRX(rightClimberCanID);
        rightClimber.setInverted(false);

        int leftClimberCanID = config.getInt("climber.leftClimber.canID", 9);
        leftClimber = new WPI_TalonSRX(leftClimberCanID);
        leftClimber.setInverted(false);

        addChild("Right Climber", rightClimber);
        addChild("Left Climber", leftClimber);
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

    public void rightClimberStop() {
        rightClimber.set(ControlMode.PercentOutput, 0);
    }

    public void leftClimberUp() {
        leftClimber.set(ControlMode.PercentOutput, 1);
    }

    public void leftClimberDown() {
        leftClimber.set(ControlMode.PercentOutput, -1);
    }

    public void leftClimberStop() {
        leftClimber.set(ControlMode.PercentOutput, 0);
    }
}