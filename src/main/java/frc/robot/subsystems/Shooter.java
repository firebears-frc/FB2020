package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.talonsrx.CurrentLimitConfiguration;
import frc.robot.util.talonsrx.StatusFrameConfiguration;
import frc.robot.util.talonsrx.TalonSRXConfiguration;

public class Shooter extends SubsystemBase {
    private static final class Constants {
        public static final int CAN_ID = 25;

        public static final double SPEED = 1.0;

        public static final TalonSRXConfiguration CONFIG = new TalonSRXConfiguration(
                true,
                NeutralMode.Coast,
                CurrentLimitConfiguration.complex(10, 25, 2000),
                StatusFrameConfiguration.normal());
    }

    private final WPI_TalonSRX motor;

    @AutoLogOutput(key = "Shooter/Speed")
    private double speed;

    public Shooter() {
        motor = new WPI_TalonSRX(Constants.CAN_ID);

        Constants.CONFIG.apply(motor);

        speed = 0.0;
    }

    public Command shoot() {
        return runOnce(() -> speed = Constants.SPEED);
    }

    public Command reverse() {
        return runOnce(() -> speed = -Constants.SPEED);
    }

    public Command stop() {
        return runOnce(() -> speed = 0.0);
    }

    @Override
    public void periodic() {
        motor.set(speed);
    }
}
