package org.firebears.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {

    private static final String TABLE_NAME = "visionTarget";

    /** Horizontal angle in degrees to the target */
    private static final String ANGLE_X = TABLE_NAME + ".angleX";

    /** Vertical angle in degrees to the target */
    private static final String ANGLE_Y = TABLE_NAME + ".angleY";

    /** Confidence that we see a valid target, in the range 0.0 to 1.0 */
    private static final String CONFIDENCE = TABLE_NAME + ".confidence";

    private final Preferences config = Preferences.getInstance();

    private final NetworkTable table;

    private final long dashDelay;
    private long dashTimeout;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Vision");
    private final NetworkTableEntry angleX;
    private final NetworkTableEntry angleY;
    private final NetworkTableEntry confidence;

    public Vision() {
        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 50;
        table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
        angleX = tab.add(ANGLE_X, 0).withPosition(0, 0).getEntry();
        angleY = tab.add(ANGLE_Y, 0).withPosition(1, 0).getEntry();
        confidence = tab.add(CONFIDENCE, 0).withPosition(0, 1).getEntry();
    }

    @Override
    public void periodic() {
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            angleX.setNumber(getTargetAngleX());
            angleY.setNumber(getTargetAngleY());
            confidence.setNumber(getTargetConfidence());
            dashTimeout = now + dashDelay;
        }
    }

    public double getTargetAngleX() {
        return table.getEntry(ANGLE_X).getDouble(0);
    }

    public double getTargetAngleY() {
        return table.getEntry(ANGLE_Y).getDouble(0);
    }

    public double getTargetConfidence() {
        return table.getEntry(CONFIDENCE).getDouble(0);
    }

    public boolean getTargetConfidenceBoolean() {
        if (getTargetConfidence() >= 1.0) {
            return true;
        } else {
            return false;
        }
    }
}
