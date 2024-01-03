package frc.robot.util.spark;

import java.util.Map;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;

public interface StatusFrameConfiguration {
    // https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
    public void apply(CANSparkBase motor);

    public static StatusFrameConfiguration normal() {
        return new StatusFrameConfiguration() {
            private static final Map<PeriodicFrame, Integer> NORMAL_FRAME_CONFIGURATION = Map.of(
                    PeriodicFrame.kStatus0, 20,
                    PeriodicFrame.kStatus1, 20,
                    PeriodicFrame.kStatus2, 20,
                    PeriodicFrame.kStatus3, 1000,
                    PeriodicFrame.kStatus4, 1000,
                    PeriodicFrame.kStatus5, 1000,
                    PeriodicFrame.kStatus6, 1000);

            @Override
            public void apply(CANSparkBase motor) {
                StatusFrameConfiguration.apply(motor, NORMAL_FRAME_CONFIGURATION);
            }
        };
    }

    public static StatusFrameConfiguration absoluteEncoder() {
        return new StatusFrameConfiguration() {
            private static final Map<PeriodicFrame, Integer> ABSOLUTE_ENCODER_CONFIGURATION = Map.of(
                    PeriodicFrame.kStatus0, 20,
                    PeriodicFrame.kStatus1, 20,
                    PeriodicFrame.kStatus2, 20,
                    PeriodicFrame.kStatus3, 1000,
                    PeriodicFrame.kStatus4, 1000,
                    PeriodicFrame.kStatus5, 20,
                    PeriodicFrame.kStatus6, 1000);

            @Override
            public void apply(CANSparkBase motor) {
                StatusFrameConfiguration.apply(motor, ABSOLUTE_ENCODER_CONFIGURATION);
            }
        };
    }

    public static StatusFrameConfiguration leadingAbsoluteEncoder() {
        return new StatusFrameConfiguration() {
            private static final Map<PeriodicFrame, Integer> LEADING_ABSOLUTE_ENCODER_CONFIGURATION = Map.of(
                    PeriodicFrame.kStatus0, 1,
                    PeriodicFrame.kStatus1, 20,
                    PeriodicFrame.kStatus2, 20,
                    PeriodicFrame.kStatus3, 1000,
                    PeriodicFrame.kStatus4, 1000,
                    PeriodicFrame.kStatus5, 20,
                    PeriodicFrame.kStatus6, 1000);

            @Override
            public void apply(CANSparkBase motor) {
                StatusFrameConfiguration.apply(motor, LEADING_ABSOLUTE_ENCODER_CONFIGURATION);
            }
        };
    }

    private static void apply(CANSparkBase motor, Map<PeriodicFrame, Integer> periods) {
        for (Map.Entry<PeriodicFrame, Integer> entry : periods.entrySet()) {
            Util.configure(period -> motor.setPeriodicFramePeriod(entry.getKey(), entry.getValue()), entry.getValue(),
                    entry.getKey().name());
        }
    }
}
