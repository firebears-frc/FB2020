package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class ClimberRight extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();
    SpeedControllerGroup group;
    private final WPI_TalonSRX rightClimber;
        public ClimberRight() {
        int rightClimberCanID = config.getInt("climber.rightClimber.canID", 8);
        rightClimber = new WPI_TalonSRX(rightClimberCanID);
        rightClimber.setInverted(false);

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

    //public void rightClimberStop() {
        //rightClimber.set(ControlMode.PercentOutput, 0);
    //}
}