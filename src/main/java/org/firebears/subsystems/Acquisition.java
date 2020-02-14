package org.firebears.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Acquisition extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();
    SpeedControllerGroup group;
    SpeedControllerGroup group1;
    /** Motor to lower acquisition system */
    private final CANSparkMax lowerMotor;
    /** Motor to spin the stars */
    private final CANSparkMax spinMotor;

    public Acquisition() {
        int acquisitionLowerMotorCanID = config.getInt("acquisition.lowerMotor.canID", 8);
        lowerMotor = new CANSparkMax(acquisitionLowerMotorCanID, MotorType.kBrushless);
        lowerMotor.setInverted(false);

        int acquisitionSpinMotorCanID = config.getInt("acquisition.spinMotor.canID", 6);
        spinMotor = new CANSparkMax(acquisitionSpinMotorCanID, MotorType.kBrushless);
        spinMotor.setInverted(false);

        group = new SpeedControllerGroup(spinMotor);
        addChild("SpinMotor", group);

        group1 = new SpeedControllerGroup(lowerMotor);
        addChild("LowerMotor", group1);
    }

    /** Periodic update */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("lowerMotorSpeed", lowerMotor.get());
        SmartDashboard.putNumber("spinMotorSpeed", spinMotor.get());
    }

    /** Start acquiring power cells */
    public void startAcquire() {
        lowerMotor.set(1.0);
        spinMotor.set(1.0);
    }

    /** Stop acquiring power cells */
    public void endAcquire() {
        lowerMotor.set(-1.0);
        spinMotor.set(0);
    }

    public void starReverse(){
        spinMotor.set(-1.0);
    }
}