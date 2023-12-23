package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.talonsrx.CurrentLimitConfiguration;
import frc.robot.util.talonsrx.StatusFrameConfiguration;
import frc.robot.util.talonsrx.TalonSRXConfiguration;

public class Feeder extends SubsystemBase {
    private static final class Constants {
        public static final int LEFT_CAN_ID = 20;
        public static final int RIGHT_CAN_ID = 21;

        public static final double SPEED = 1.0;

        public static final TalonSRXConfiguration LEFT_CONFIG = new TalonSRXConfiguration(
                true,
                NeutralMode.Coast,
                CurrentLimitConfiguration.complex(10, 15, 5000),
                StatusFrameConfiguration.normal());
        public static final TalonSRXConfiguration RIGHT_CONFIG = new TalonSRXConfiguration(
                true,
                NeutralMode.Coast,
                CurrentLimitConfiguration.complex(10, 15, 5000),
                StatusFrameConfiguration.normal());
    }

    private final WPI_TalonSRX leftMotor, rightMotor;
    private final MotorControllerGroup motors;

    @AutoLogOutput(key = "Feeder/Speed")
    private double speed;

    public Feeder() {
        leftMotor = new WPI_TalonSRX(Constants.LEFT_CAN_ID);
        rightMotor = new WPI_TalonSRX(Constants.RIGHT_CAN_ID);

        Constants.LEFT_CONFIG.apply(leftMotor);
        Constants.RIGHT_CONFIG.apply(rightMotor);

        motors = new MotorControllerGroup(leftMotor, rightMotor);

        speed = 0.0;
    }

    public Command run() {
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
        motors.set(speed);
    }
}
