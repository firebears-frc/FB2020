package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase {

    static private final int PID_LOOP_IDX = 0;
    static private final double RPM = 350.0 * 5.0;
    static private final double SENSOR_UNITS_PER_REV = 4096;
    static private final double GEAR_RATIO = 13.56;
    static private final double PER_MINUTE_100_MS = 600.0;

    private double targetVelocity = 0;
    private final Preferences config = Preferences.getInstance();

    private final TalonSRX srx;

    public Shooter() {
        super();
        int timeoutMs = config.getInt("srx.timeout", 30);
        int peakCurrentLimit = config.getInt("shooter.peakCurrentLimit", 25);
        int peakCurrentDuration = config.getInt("shooter.peakCurrentDuration",
            2000);
        int continuousCurrentLimit = config.getInt(
            "shooter.continuousCurrentLimit", 10);
        double p = config.getDouble("shooter.P", 1.0);
        double i = config.getDouble("shooter.I", 0.0);
        double d = config.getDouble("shooter.D", 0.0);
        double f = config.getDouble("shooter.F", 0.0);
        srx = new TalonSRX(config.getInt("shooter.motor1", 25));
        srx.configFactoryDefault();
        srx.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
            PID_LOOP_IDX, timeoutMs);
        srx.setSensorPhase(true);
        // Configure nominal / peak outputs
        srx.configNominalOutputForward(0, timeoutMs);
        srx.configNominalOutputReverse(0, timeoutMs);
        srx.configPeakOutputForward(1, timeoutMs);
        srx.configPeakOutputReverse(1, timeoutMs);
        // Configure limits
        srx.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        srx.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        srx.configContinuousCurrentLimit(continuousCurrentLimit, timeoutMs);
        // Config PIDF
        srx.config_kP(PID_LOOP_IDX, p, timeoutMs);
        srx.config_kI(PID_LOOP_IDX, i, timeoutMs);
        srx.config_kD(PID_LOOP_IDX, d, timeoutMs);
        srx.config_kF(PID_LOOP_IDX, f, timeoutMs);
    }

    public void periodic() {
        double output = srx.getMotorOutputPercent();
        SmartDashboard.putNumber("Output", output);
        int velocity = srx.getSelectedSensorVelocity(PID_LOOP_IDX);
        SmartDashboard.putNumber("Velocity", velocity);
        // velocity in units per 100 ms
        srx.set(ControlMode.Velocity, targetVelocity);
    }

    public boolean isWheelSpunUp() {
        return srx.getSelectedSensorVelocity(PID_LOOP_IDX) >= targetVelocity;
    }
    
    public void setTargetRPM(double RPM){
        targetVelocity = RPM * SENSOR_UNITS_PER_REV /
        (PER_MINUTE_100_MS * GEAR_RATIO);
    }
}
