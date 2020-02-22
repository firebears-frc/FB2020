package org.firebears.subsystems;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.firebears.Robot;
import org.firebears.commands.AcquisitionRaiseCommand;

public class Acquisition extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();

    private final SpeedControllerGroup group;
    private final SpeedControllerGroup group1;
    /** Motor to lower acquisition system */
    private final CANSparkMax lowerMotor;
    /** Motor to spin the stars */
    private final CANSparkMax spinMotor;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Acquisition");
    private final NetworkTableEntry lowerMotorSpeed;
    private final NetworkTableEntry spinMotorSpeed;

    private final long dashDelay;
    private long dashTimeout = 0;

    public Acquisition() {
        CANError err;
        int lowerStallLimit = config.getInt("acquisition.lowerStallLimit", 20);
        int lowerFreeLimit = config.getInt("acquisition.lowerFreeLimit", 10);
        int lowerLimitRPM = config.getInt("acquisition.lowerLimitRPM", 500);
        int spinStallLimit = config.getInt("acquisition.spinStallLimit", 10);
        int spinFreeLimit = config.getInt("acquisition.spinFreeLimit", 10);
        int spinLimitRPM = config.getInt("acquisition.spinLimitRPM", 500);
        int acquisitionLowerMotorCanID = config.getInt("acquisition.lowerMotor.canID", 11);
        int acquisitionSpinMotorCanID = config.getInt("acquisition.spinMotor.canID", 12);

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 150;

        lowerMotor = new CANSparkMax(acquisitionLowerMotorCanID, MotorType.kBrushless);
        lowerMotor.setInverted(false);
        err = lowerMotor.setSmartCurrentLimit(lowerStallLimit, lowerFreeLimit, lowerLimitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on lowerMotor");

        spinMotor = new CANSparkMax(acquisitionSpinMotorCanID, MotorType.kBrushless);
        spinMotor.setInverted(false);
        err = spinMotor.setSmartCurrentLimit(spinStallLimit, spinFreeLimit, spinLimitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on spinMotor");

        group = new SpeedControllerGroup(spinMotor);
        addChild("SpinMotor", group);

        group1 = new SpeedControllerGroup(lowerMotor);
        addChild("LowerMotor", group1);

        lowerMotorSpeed = tab.add("lower motor speed", 0).withPosition(0, 0).getEntry();
        spinMotorSpeed = tab.add("spin motor speed", 0).withPosition(0, 1).getEntry();
        setDefaultCommand(new AcquisitionRaiseCommand(
            this, Robot.loader));
    }

    /** Periodic update */
    @Override
    public void periodic() {
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            lowerMotorSpeed.setNumber(lowerMotor.get());
            spinMotorSpeed.setNumber(spinMotor.get());
            dashTimeout = now + dashDelay;
        }
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

    public void starReverse() {
        lowerMotor.set(0.0);
        spinMotor.set(-1.0);
    }
}
