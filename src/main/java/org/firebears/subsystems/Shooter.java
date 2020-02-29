package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import org.firebears.Robot;
import org.firebears.util.RangeVelocityTable;

public class Shooter extends SubsystemBase {

    static private final int PID_LOOP_IDX = 0;
    static private final double SENSOR_UNITS_PER_REV = 4096;
    static private final double GEAR_RATIO = 2.5;
    static private final double PER_MINUTE_100_MS = 600.0;
    static private final double DEFAULT_RANGE_FT = 10.0;
    static private final double FEET_PER_METER = 0.3048;
    static private final double DEFAULT_RANGE_M =
        DEFAULT_RANGE_FT * FEET_PER_METER;

    static private final double WHEEL_RADIUS_FT = 0.25;
    static private final double WHEEL_RADIUS_M =
        WHEEL_RADIUS_FT * FEET_PER_METER;

    /** Estamate of velocity loss from shooter */ 
    static private final double LOSS_COEFFICIENT = 0.45;

    /** Speed (power cell) when idling (m/s) */
    static private final double IDLE_SPEED = -3;

    /** Table to convert range to velocity */
    private final RangeVelocityTable range_velocity;

    /** Target power cell velocity in m/s */
    private double powerCellVelocity = 0;

    private double targetRpm = 0;

    /** Target velocity in ticks */
    private double targetVelocity = 0;

    private final Preferences config = Preferences.getInstance();

    private final ShuffleboardTab tab = Shuffleboard.getTab("Shooter");
    private final NetworkTableEntry outputWidget;
    private final NetworkTableEntry velocityWidget;
    private final NetworkTableEntry targetVelocityWidget;
    private final NetworkTableEntry powerCellVelocityWidget;
    private final NetworkTableEntry targetRpmWidget;
    private final NetworkTableEntry isSpunWidget;

    private final TalonSRX srx;

    private final long dashDelay;
    private long dashTimeout = 0;

    public Shooter() {
        super();
        int peakCurrentLimit = config.getInt("shooter.peakCurrentLimit", 25);
        int peakCurrentDuration = config.getInt("shooter.peakCurrentDuration",
            2000);
        int continuousCurrentLimit = config.getInt(
            "shooter.continuousCurrentLimit", 10);
        double p = config.getDouble("shooter.P", 1.0);
        double i = config.getDouble("shooter.I", 0.0);
        double d = config.getDouble("shooter.D", 0.0);
        double f = config.getDouble("shooter.F", 0.0);
        range_velocity = RangeVelocityTable.load(
            config.getInt("shooter.angle", 45));
        int timeoutMs = config.getInt("srx.timeout", 30);
        srx = new TalonSRX(config.getInt("shooter.motor1", 25));
        srx.setInverted(true);
        srx.configFactoryDefault();
        srx.configSelectedFeedbackSensor(FeedbackDevice.SensorSum,
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

        outputWidget = tab.add("output", 0).withPosition(0, 0).getEntry();
        velocityWidget = tab.add("velocity", 0).withPosition(0, 1).getEntry();
        targetVelocityWidget = tab.add("target velocity", 0)
            .withPosition(1, 0).getEntry();
        powerCellVelocityWidget = tab.add("power cell velocity", 0)
            .withPosition(2, 0).getEntry();
        targetRpmWidget = tab.add("target RPM", 0).withPosition(2, 1)
            .getEntry();
        isSpunWidget = tab.add("isWheelSpunUp", false).withPosition(3, 0)
            .getEntry();

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 200;
    }

    @Override
    public void periodic() {
        double output = srx.getMotorOutputPercent();
        int velocity = srx.getSelectedSensorVelocity(PID_LOOP_IDX);
        // velocity in units per 100 ms
        //srx.set(ControlMode.Velocity, targetVelocity);
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            outputWidget.setNumber(output);
            velocityWidget.setNumber(velocity);
            targetVelocityWidget.setNumber(targetVelocity);
            powerCellVelocityWidget.setNumber(powerCellVelocity);
            targetRpmWidget.setNumber(targetRpm);
            isSpunWidget.setBoolean(isWheelSpunUp());
            dashTimeout = now + dashDelay;
        }
    }

    public boolean isWheelSpunUp() {
        return srx.getSelectedSensorVelocity(PID_LOOP_IDX) >= targetVelocity;
    }

    public void spinUp() {
        srx.set(ControlMode.PercentOutput, 1.0);
        /*double range = Robot.lidar.getDistance();
        if (range < 0)
            range = DEFAULT_RANGE_M;
        powerCellVelocity = optimalVelocity(range);
        targetRpm = calcRpm();
        targetVelocity = targetVelocity();
        */
    }

    public void idle() {
        srx.set(ControlMode.PercentOutput, 0);
        powerCellVelocity = IDLE_SPEED;
        targetRpm = calcRpm();
        targetVelocity = targetVelocity();
    }

    /** Calculate the optimal power cell velocity in m/s
     * @param range Distance in meters */
    private double optimalVelocity(double range) {
        return range_velocity.getOptimal(range);
    }

    /** Calculate the RPM needed for a given power cell speed */
    private double calcRpm() {
        // angular velocity of wheel in radians/second
        double angularVelocity = powerCellVelocity * LOSS_COEFFICIENT
            / WHEEL_RADIUS_M;
        // revolutions per second
        double rps = Math.toDegrees(angularVelocity) / 360.0;
        return rps * 60.0;
    }

    private double targetVelocity() {
        return targetRpm * SENSOR_UNITS_PER_REV /
            (PER_MINUTE_100_MS * GEAR_RATIO);
    }
}
