package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private static class Constants {
        public static final int CAN_ID = 10;

        public static final double SPEED = 1.0;

        public static final int PEAK_CURRENT_LIMIT = 15;
        public static final int PEAK_CURRENT_DURATION = 5000;
        public static final int CONTINUOUS_CURRENT_LIMIT = 10;
    }

    private final WPI_TalonSRX motor;

    public Intake() {
        motor = new WPI_TalonSRX(Constants.CAN_ID);
        motor.configFactoryDefault();
        motor.setInverted(false);
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configPeakCurrentLimit(Constants.PEAK_CURRENT_LIMIT);
        motor.configPeakCurrentDuration(Constants.PEAK_CURRENT_DURATION);
        motor.configContinuousCurrentLimit(Constants.CONTINUOUS_CURRENT_LIMIT);
    }

    private void set(double speed) {
        motor.set(ControlMode.PercentOutput, speed);
    }

    public Command intake() {
        return new StartEndCommand(() -> {
            set(Constants.SPEED);
        }, () -> {
            set(0.0);
        }, this);
    }

    public Command reverse() {
        return new StartEndCommand(() -> {
            set(Constants.SPEED * -1.0);
        }, () -> {
            set(0.0);
        }, this);
    }
}
