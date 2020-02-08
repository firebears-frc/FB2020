package org.firebears.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Acquisition extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();

    /** Motor to lower acquisition system */
    private final CANSparkMax lower;

    /** Motor to spin the stars */
    private final CANSparkMax spin;

    public Acquisition() {
        int acquisitionLowerCanID = config.getInt("acquisition.lower.canID", 8);
        lower = new CANSparkMax(acquisitionLowerCanID, MotorType.kBrushless);
        lower.setInverted(false);

        int acquisitionSpinCanID = config.getInt("acquisition.spin.canID", 6);
        spin = new CANSparkMax(acquisitionSpinCanID, MotorType.kBrushless);
        spin.setInverted(false);
    }

    /** Periodic update */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("lowerSpeed", lower.get());
        SmartDashboard.putNumber("spinSpeed", spin.get());
    }

    /** Start acquiring power cells */
    public void startAcquire() {
        lower.set(1);
        spin.set(1);
    }

    /** Stop acquiring power cells */
    public void endAcquire() {
        lower.set(-1);
        spin.set(0);
    }
}