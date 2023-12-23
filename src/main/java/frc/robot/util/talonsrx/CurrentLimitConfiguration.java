package frc.robot.util.talonsrx;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public interface CurrentLimitConfiguration {
    public void apply(WPI_TalonSRX motor);

    public static CurrentLimitConfiguration simple(int continuousLimit) {
        return new CurrentLimitConfiguration() {
            @Override
            public void apply(WPI_TalonSRX motor) {
                Util.configure(motor::configContinuousCurrentLimit, continuousLimit, "continuousLimit");
            }
        };
    }

    public static CurrentLimitConfiguration complex(int continuousLimit, int peakLimit, int peakDuration) {
        return new CurrentLimitConfiguration() {
            @Override
            public void apply(WPI_TalonSRX motor) {
                Util.configure(motor::configContinuousCurrentLimit, continuousLimit, "continuousLimit");
                Util.configure(motor::configPeakCurrentLimit, peakLimit, "peakLimit");
                Util.configure(motor::configPeakCurrentDuration, peakDuration, "peakDuration");
            }
        };
    }
}
