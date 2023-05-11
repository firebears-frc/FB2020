package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    private static class Constants {
        public static final int PORT = 12;

        public static final double SPEED = 0.4;
    }

    private final CANSparkMax motor;

    public Indexer() {
        motor = new CANSparkMax(Constants.PORT, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        motor.setInverted(false);
        motor.setIdleMode(IdleMode.kBrake);

        motor.burnFlash();
    }

    private void set(double speed) {
        motor.set(speed);
    }

    public Command run() {
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
