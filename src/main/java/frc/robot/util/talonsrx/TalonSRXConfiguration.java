package frc.robot.util.talonsrx;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonSRXConfiguration {
    private final boolean inverted;
    private final NeutralMode neutralMode;
    private final CurrentLimitConfiguration currentLimits;
    private final StatusFrameConfiguration statusFrames;

    public TalonSRXConfiguration(boolean inverted, NeutralMode neutralMode, CurrentLimitConfiguration currentLimits,
            StatusFrameConfiguration statusFrames) {
        this.inverted = inverted;
        this.neutralMode = neutralMode;
        this.currentLimits = currentLimits;
        this.statusFrames = statusFrames;
    }

    public void apply(WPI_TalonSRX motor) {
        Util.configure(motor::configFactoryDefault, 0, "configFactoryDefault");
        Util.configureAndVerify(motor::setInverted, motor::getInverted, inverted, "inverted");
        Util.configureAndVerify(motor::setNeutralMode, () -> neutralMode, neutralMode, "neutralMode");
        currentLimits.apply(motor);
        statusFrames.apply(motor);
    }

    public void apply(WPI_TalonSRX... motors) {
        for (WPI_TalonSRX motor : motors) {
            apply(motor);
        }
    }
}
