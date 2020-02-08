package org.firebears.subsystems;

//import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision extends SubsystemBase {

    NetworkTable visionTargetTable;

    public static final String VISION_TARGET_TABLE_NAME = "visionTarget";

    /** Horizontal angle in degrees to the target. */
    public static final String VISION_TARGET_ANGLE_X = "visionTarget.angleX";

    /** Vertical angle in degrees to the target. */
    public static final String VISION_TARGET_ANGLE_Y = "visionTarget.angleY";

    /** Confidence that we see a valid target, in the range 0.0 to 1.0. */
    public static final String VISION_TARGET_CONFIDENCE = "visionTarget.confidence";

    /** Number of vision target pairs */
    public static final String VISION_TARGET_PAIRS = "visionTarget.pairs";

    /** Processing throughout in frames per second. */
    public static final String VISION_TARGET_FPS = "visionTarget.fps";

    public static final String VISION_TARGET_SAVE_IMAGE_TIME = "visionTarget.saveImageTime";

    public Vision() {
        visionTargetTable = NetworkTableInstance.getDefault().getTable(VISION_TARGET_TABLE_NAME);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber(VISION_TARGET_ANGLE_X, getVisionTargetAngleX());
        SmartDashboard.putNumber(VISION_TARGET_CONFIDENCE, getVisionTargetConfidence());
    }

    public double getVisionTargetAngleX() {
        return visionTargetTable.getEntry(VISION_TARGET_ANGLE_X).getDouble(0);
    }

    public double getVisionTargetAngleY() {
        return visionTargetTable.getEntry(VISION_TARGET_ANGLE_Y).getDouble(0);
    }

    public double getVisionTargetConfidence() {
        return visionTargetTable.getEntry(VISION_TARGET_CONFIDENCE).getDouble(0);
    }

    public double getVisionTargetPairs() {
        return visionTargetTable.getEntry(VISION_TARGET_PAIRS).getDouble(0);
    }

    public double getVisionTargetFPS() {
        return visionTargetTable.getEntry(VISION_TARGET_PAIRS).getDouble(0);
    }

    public void setVisionTargetSaveImageTime(double miliSeconds) {
        visionTargetTable.getEntry(VISION_TARGET_SAVE_IMAGE_TIME).setNumber(miliSeconds);
    }

    public boolean getVisionTargetConfidenceBoolean() {
        if (getVisionTargetConfidence() == 1) {
            return true;
        } else {
            return false;
        }
    }
}