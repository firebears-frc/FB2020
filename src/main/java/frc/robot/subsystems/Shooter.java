package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private static class Constants {
        public static final int CAN_ID = 25;

        public static final double SPEED = 1.0;

        public static final int PEAK_CURRENT_LIMIT = 25;
        public static final int PEAK_CURRENT_DURATION = 2000;
        public static final int CONTINUOUS_CURRENT_LIMIT = 10;
    }

    private final WPI_TalonSRX motor;

    @AutoLogOutput(key = "Shooter/Speed")
    private double speed;

    public Shooter() {
        motor = new WPI_TalonSRX(Constants.CAN_ID);
        motor.configFactoryDefault();
        motor.setInverted(true);
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configPeakCurrentLimit(Constants.PEAK_CURRENT_LIMIT);
        motor.configPeakCurrentDuration(Constants.PEAK_CURRENT_DURATION);
        motor.configContinuousCurrentLimit(Constants.CONTINUOUS_CURRENT_LIMIT);

        // https://v5.docs.ctr-electronics.com/en/stable/ch18_CommonAPI.html#setting-status-frame-periods
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 20);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, 1000);
        motor.setStatusFramePeriod(StatusFrameEnhanced.Status_21_FeedbackIntegrated, 1000);

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
