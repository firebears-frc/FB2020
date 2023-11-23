package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static class Constants {
        public static final int LEFT_CAN_ID = 20;
        public static final int RIGHT_CAN_ID = 21;

        public static final double SPEED = 1.0;

        public static final int PEAK_CURRENT_LIMIT = 15;
        public static final int PEAK_CURRENT_DURATION = 5000;
        public static final int CONTINUOUS_CURRENT_LIMIT = 10;
    }

    private final WPI_TalonSRX leftMotor, rightMotor;
    private final MotorControllerGroup motors;

    public Feeder() {
        leftMotor = new WPI_TalonSRX(Constants.LEFT_CAN_ID);
        leftMotor.configFactoryDefault();
        leftMotor.setInverted(true);
        leftMotor.setNeutralMode(NeutralMode.Coast);
        leftMotor.configPeakCurrentLimit(Constants.PEAK_CURRENT_LIMIT);
        leftMotor.configPeakCurrentDuration(Constants.PEAK_CURRENT_DURATION);
        leftMotor.configContinuousCurrentLimit(Constants.CONTINUOUS_CURRENT_LIMIT);

        rightMotor = new WPI_TalonSRX(Constants.RIGHT_CAN_ID);
        rightMotor.configFactoryDefault();
        rightMotor.setInverted(false);
        rightMotor.setNeutralMode(NeutralMode.Coast);
        rightMotor.configPeakCurrentLimit(Constants.PEAK_CURRENT_LIMIT);
        rightMotor.configPeakCurrentDuration(Constants.PEAK_CURRENT_DURATION);
        rightMotor.configContinuousCurrentLimit(Constants.CONTINUOUS_CURRENT_LIMIT);

        motors = new MotorControllerGroup(leftMotor, rightMotor);
    }

    public Command run() {
        return runOnce(() -> motors.set(Constants.SPEED));
    }

    public Command reverse() {
        return runOnce(() -> motors.set(-Constants.SPEED));
    }

    public Command stop() {
        return runOnce(() -> motors.set(0.0));
    }
}
