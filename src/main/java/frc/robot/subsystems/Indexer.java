package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

    public Indexer() {
        motor = new CANSparkMax(Constants.CAN_ID, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        motor.setInverted(false);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        motor.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        positionSensor = new DigitalInput(Constants.POSITION_SENSOR_PORT);

        motor.burnFlash();
    }

    public Command advance() {
        return runEnd(() -> motor.set(Constants.SPEED), () -> motor.set(0.0)).until(positionSensor::get);
    }

    public Command reverse() {
        return runEnd(() -> motor.set(-Constants.SPEED), () -> motor.set(0.0)).until(positionSensor::get);
    }
}
