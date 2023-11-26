package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static final class Constants {
        public static final int LEFT_CAN_ID = 20;
        public static final int RIGHT_CAN_ID = 21;

        public static final double SPEED = 1.0;

        public static final int PEAK_CURRENT_LIMIT = 15;
        public static final int PEAK_CURRENT_DURATION = 5000;
        public static final int CONTINUOUS_CURRENT_LIMIT = 10;
    }

    private final WPI_TalonSRX leftMotor, rightMotor;
    private final MotorControllerGroup motors;

    @AutoLogOutput(key = "Feeder/Speed")
    private double speed;

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

        // https://v5.docs.ctr-electronics.com/en/stable/ch18_CommonAPI.html#setting-status-frame-periods
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 20);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, 1000);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_21_FeedbackIntegrated, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 20);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, 1000);
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_21_FeedbackIntegrated, 1000);

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
