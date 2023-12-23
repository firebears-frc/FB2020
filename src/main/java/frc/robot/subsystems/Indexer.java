package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.sparkmax.CurrentLimitConfiguration;
import frc.robot.util.sparkmax.SparkMaxConfiguration;
import frc.robot.util.sparkmax.StatusFrameConfiguration;

public class Indexer extends SubsystemBase {
    private static final class Constants {
        public static final int CAN_ID = 12;

        public static final int POSITION_SENSOR_PORT = 0;

        public static final double SPEED = 0.4;

        public static final SparkMaxConfiguration CONFIG = new SparkMaxConfiguration(
                false,
                IdleMode.kBrake,
                CurrentLimitConfiguration.complex(20, 10, 10, 25.0),
                StatusFrameConfiguration.normal());
    }

    private final CANSparkMax motor;
    private final DigitalInput positionSensor;

    @AutoLogOutput(key = "Indexer/Speed")
    private double speed;

    public Indexer() {
        motor = new CANSparkMax(Constants.CAN_ID, MotorType.kBrushless);

        Constants.CONFIG.apply(motor);

        positionSensor = new DigitalInput(Constants.POSITION_SENSOR_PORT);

        speed = 0.0;
    }

    @AutoLogOutput(key = "Indexer/Sensor")
    private boolean sensor() {
        return positionSensor.get();
    }

    public Command advance() {
        return runEnd(() -> speed = Constants.SPEED, () -> speed = 0.0).until(this::sensor);
    }

    public Command reverse() {
        return runEnd(() -> speed = -Constants.SPEED, () -> speed = 0.0).until(this::sensor);
    }

    @Override
    public void periodic() {
        motor.set(speed);
    }
}
