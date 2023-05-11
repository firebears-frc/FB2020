package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private static class Constants {
        public static final int PORT = 25;

        public static final double SPEED = 1.0;
    }
    private final WPI_TalonSRX motor;

    public Shooter() {
        motor = new WPI_TalonSRX(Constants.PORT);
        motor.configFactoryDefault();
        motor.setInverted(true);
        motor.setNeutralMode(NeutralMode.Coast);
    }

    private void set(double speed) {
        motor.set(ControlMode.PercentOutput, speed);
    }

    public Command shoot() {
        return new StartEndCommand(() -> {
            set(Constants.SPEED);
        }, () -> {
            set(0.0);
        }, this);
    }
}
