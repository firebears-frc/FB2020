package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private static class Constants {
        public static final int PORT = 25;

        public static final double SPEED = 1.0;
    }

    private final TalonSRX motor;

    public Shooter() {
        motor = new TalonSRX(Constants.PORT);
        motor.configFactoryDefault();
        motor.setInverted(true);
    }

    private void set(double speed) {
        motor.set(TalonSRXControlMode.PercentOutput, speed);
    }

    public Command shoot() {
        return new StartEndCommand(() -> {
            set(Constants.SPEED);
        }, () -> {
            set(0.0);
        }, this);
    }
}
