package org.firebears.subsystems;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Acquisition extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();

    private final SpeedControllerGroup group;
    private final SpeedControllerGroup group1;
    /** Motor to lower acquisition system */
    private final CANSparkMax lowerMotor;
    /** Motor to spin the stars */
    private final CANSparkMax spinMotor;

    public Acquisition() {
        CANError err;
        int lowerStallLimit = config.getInt("acquisition.lowerStallLimit", 20);
        int lowerFreeLimit = config.getInt("acquisition.lowerFreeLimit", 10);
        int lowerLimitRPM = config.getInt("acquisition.lowerLimitRPM", 500);
        int spinStallLimit = config.getInt("acquisition.spinStallLimit", 10);
        int spinFreeLimit = config.getInt("acquisition.spinFreeLimit", 10);
        int spinLimitRPM = config.getInt("acquisition.spinLimitRPM", 500);
        
        int acquisitionLowerMotorCanID = config.getInt("acquisition.lowerMotor.canID", 11);
        lowerMotor = new CANSparkMax(acquisitionLowerMotorCanID, MotorType.kBrushless);
        lowerMotor.setInverted(false);
        err = lowerMotor.setSmartCurrentLimit(lowerStallLimit, lowerFreeLimit, lowerLimitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on lowerMotor");

        int acquisitionSpinMotorCanID = config.getInt("acquisition.spinMotor.canID", 12);
        spinMotor = new CANSparkMax(acquisitionSpinMotorCanID, MotorType.kBrushless);
        spinMotor.setInverted(false);
        err = spinMotor.setSmartCurrentLimit(spinStallLimit, spinFreeLimit, spinLimitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on spinMotor");

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