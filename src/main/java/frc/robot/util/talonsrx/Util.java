package frc.robot.util.talonsrx;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.ctre.phoenix.ErrorCode;

import edu.wpi.first.wpilibj.DriverStation;

public class Util {
    private static class Constants {
        public static final int MAXIMUM_RETRIES = 5;
    }

    static <T> void configure(Function<T, ErrorCode> setter, T setting, String name) {
        for (int i = 0; i < Constants.MAXIMUM_RETRIES; i++) {
            // Apply setting and check if the library returned OK status
            if (setter.apply(setting) == ErrorCode.OK) {
                return; // If it applied ok, exit
            }
            // Otherwise, try again
        }
        // We've hit max retries
        DriverStation.reportWarning("Failed to set parameter '" + name + "'", true);
    }

    static <T> void configureAndVerify(Consumer<T> setter, Supplier<T> getter, T setting, String name) {
        for (int i = 0; i < Constants.MAXIMUM_RETRIES; i++) {
            // Apply setting
            setter.accept(setting);
            // Check if the setting stuck
            if (getter.get() == setting)
                return; // If it stuck, exit
            // Otherwise, try again
        }
        // We've hit max retries
        DriverStation.reportWarning("Failed to set parameter '" + name + "'", true);
    }

    static <T> void configureCheckAndVerify(Function<T, ErrorCode> setter, Supplier<T> getter, T setting, String name) {
        for (int i = 0; i < Constants.MAXIMUM_RETRIES; i++) {
            // Apply setting and check if the library returned OK status
            if (setter.apply(setting) == ErrorCode.OK) {
                // Check if the setting stuck
                if (getter.get() == setting) {
                    return; // If it applied ok and stuck, exit
                }
            }
            // Otherwise, try again
        }
        // We've hit max retries
        DriverStation.reportWarning("Failed to set parameter '" + name + "'", true);
    }
}
