package frc.robot.util.sparkmax;

import com.revrobotics.CANSparkMax;
import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;

public class SparkMaxConfiguration {
    private final boolean inverted;
    private final IdleMode idleMode;
    private final CurrentLimitConfiguration currentLimits;
    private final StatusFrameConfiguration statusFrames;
    private final ClosedLoopConfiguration closedLoop;
    private final FeedbackConfiguration feedback;
    private final FollowingConfiguration following;

    public SparkMaxConfiguration(boolean inverted, IdleMode idleMode, CurrentLimitConfiguration currentLimits,
            StatusFrameConfiguration statusFrames) {
        this.inverted = inverted;
        this.idleMode = idleMode;
        this.currentLimits = currentLimits;
        this.statusFrames = statusFrames;
        this.closedLoop = null;
        this.feedback = null;
        this.following = null;
    }

    public SparkMaxConfiguration(boolean inverted, IdleMode idleMode, CurrentLimitConfiguration currentLimits,
            StatusFrameConfiguration statusFrames, ClosedLoopConfiguration closedLoop, FeedbackConfiguration feedback) {
        this.inverted = inverted;
        this.idleMode = idleMode;
        this.currentLimits = currentLimits;
        this.statusFrames = statusFrames;
        this.closedLoop = closedLoop;
        this.feedback = feedback;
        this.following = null;
    }

    public SparkMaxConfiguration(boolean inverted, IdleMode idleMode, CurrentLimitConfiguration currentLimits,
            StatusFrameConfiguration statusFrames, FollowingConfiguration following) {
        this.inverted = inverted;
        this.idleMode = idleMode;
        this.currentLimits = currentLimits;
        this.statusFrames = statusFrames;
        this.closedLoop = null;
        this.feedback = null;
        this.following = following;
    }

    public void apply(CANSparkMax motor) {
        Util.configure(motor::restoreFactoryDefaults, false, "restoreFactoryDefaults");
        Util.configureAndVerify(motor::setInverted, motor::getInverted, inverted, "inverted");
        Util.configureCheckAndVerify(motor::setIdleMode, motor::getIdleMode, idleMode, "idleMode");
        currentLimits.apply(motor);
        statusFrames.apply(motor);
        if (closedLoop != null && feedback != null) {
            SparkMaxPIDController pid = closedLoop.apply(motor);
            MotorFeedbackSensor sensor = feedback.apply(motor);
            pid.setFeedbackDevice(sensor);
        } else if (following != null) {
            following.apply(motor);
        }
        Util.burnFlash(motor);
    }
}
