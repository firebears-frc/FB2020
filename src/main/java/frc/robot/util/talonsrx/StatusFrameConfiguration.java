package frc.robot.util.talonsrx;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public interface StatusFrameConfiguration {
    // https://v5.docs.ctr-electronics.com/en/stable/ch18_CommonAPI.html#setting-status-frame-periods
    public void apply(WPI_TalonSRX motor);

    public static StatusFrameConfiguration normal() {
        return new StatusFrameConfiguration() {
            private static final Map<StatusFrameEnhanced, Integer> NORMAL_FRAME_CONFIGURATION = Map.of(
                    StatusFrameEnhanced.Status_1_General, 20,
                    StatusFrameEnhanced.Status_2_Feedback0, 20,
                    StatusFrameEnhanced.Status_3_Quadrature, 1000,
                    StatusFrameEnhanced.Status_4_AinTempVbat, 20,
                    StatusFrameEnhanced.Status_8_PulseWidth, 1000,
                    StatusFrameEnhanced.Status_10_MotionMagic, 1000,
                    StatusFrameEnhanced.Status_12_Feedback1, 1000,
                    StatusFrameEnhanced.Status_13_Base_PIDF0, 1000,
                    StatusFrameEnhanced.Status_14_Turn_PIDF1, 1000,
                    StatusFrameEnhanced.Status_21_FeedbackIntegrated, 1000);

            @Override
            public void apply(WPI_TalonSRX motor) {
                StatusFrameConfiguration.apply(motor, NORMAL_FRAME_CONFIGURATION);
            }
        };
    }

    private static void apply(WPI_TalonSRX motor, Map<StatusFrameEnhanced, Integer> periods) {
        for (Map.Entry<StatusFrameEnhanced, Integer> entry : periods.entrySet()) {
            Util.configureCheckAndVerify(
                    setting -> motor.setStatusFramePeriod(entry.getKey(), setting),
                    () -> motor.getStatusFramePeriod(entry.getKey()),
                    entry.getValue(),
                    entry.getKey().name());
        }
    }
}
