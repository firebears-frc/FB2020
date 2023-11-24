package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    private static class Constants {
        public static final int CAN_ID = 12;

        public static final int POSITION_SENSOR_PORT = 0;

        public static final double SPEED = 0.4;

        public static final int STALL_CURRENT_LIMIT = 20;
        public static final int FREE_CURRENT_LIMIT = 10;
        public static final double SECONDARY_CURRENT_LIMIT = 25.0;
    }

    private final CANSparkMax motor;
    private final DigitalInput positionSensor;

    @AutoLogOutput(key = "Indexer/Speed")
    private double speed;

    public Indexer() {
        motor = new CANSparkMax(Constants.CAN_ID, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        motor.setInverted(false);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        motor.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        positionSensor = new DigitalInput(Constants.POSITION_SENSOR_PORT);

        motor.burnFlash();

        // https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        motor.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);

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
