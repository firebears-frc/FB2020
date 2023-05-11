package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static class Constants {
        public static final int LEFT_PORT = 20;
        public static final int RIGHT_PORT = 21;

        public static final double SPEED = 1.0;
    }

    private final WPI_TalonSRX left;
    private final WPI_TalonSRX right;

    public Feeder() {
        left = new WPI_TalonSRX(Constants.LEFT_PORT);
        left.configFactoryDefault();
        left.setInverted(true);
        left.setNeutralMode(NeutralMode.Coast);

        right = new WPI_TalonSRX(Constants.RIGHT_PORT);
        right.configFactoryDefault();
        right.setInverted(false);
        right.setNeutralMode(NeutralMode.Coast);
    }

    private void set(double speed) {
        left.set(ControlMode.PercentOutput, speed);
        right.set(ControlMode.PercentOutput, speed);
    }

    public Command run() {
        return new StartEndCommand(() -> {
            set(Constants.SPEED);
        }, () -> {
            set(0.0);
        }, this);
    }
}
