package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Acquisition extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();

    private SparkMaxLimitSwitch limitUp;
    private SparkMaxLimitSwitch limitDown;

    //private final SpeedControllerGroup group;
    private final SpeedControllerGroup group1;
    /** Motor to lower acquisition system */
    private final CANSparkMax lowerMotor;
    /** Motor to spin the stars */
   
    private final WPI_TalonSRX spinMotor;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Acquisition");
    private final NetworkTableEntry lowerMotorSpeed;
    private final NetworkTableEntry spinMotorSpeed;
    private final NetworkTableEntry upWidget;
    private final NetworkTableEntry downWidget;

    private final long dashDelay;
    private long dashTimeout = 0;

    


    public Acquisition() {
        REVLibError err;
        int lowerStallLimit = config.getInt("acquisition.lowerStallLimit", 20);
        int lowerFreeLimit = config.getInt("acquisition.lowerFreeLimit", 10);
        int lowerLimitRPM = config.getInt("acquisition.lowerLimitRPM", 500);
        int spinStallLimit = config.getInt("acquisition.spinStallLimit", 10);
        int spinFreeLimit = config.getInt("acquisition.spinFreeLimit", 10);
        int spinLimitRPM = config.getInt("acquisition.spinLimitRPM", 500);
        int acquisitionLowerMotorCanID = config.getInt("acquisition.lowerMotor.canID", 11);
        int acquisitionSpinMotorCanID = config.getInt("acquisition.spinMotor.canID", 10);
        int timeoutMs = config.getInt("stars.timeout", 30);
        int peakCurrentLimit = config.getInt("stars.peakCurrentLimit", 15);
        int peakCurrentDuration = config.getInt("stars.peakCurrentDuration", 5000);
        int continuousCurrentLimit = config.getInt("stars.continuousCurrentLimit", 10);
       

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 150;

        lowerMotor = new CANSparkMax(acquisitionLowerMotorCanID, MotorType.kBrushless);
        lowerMotor.restoreFactoryDefaults();
        lowerMotor.setInverted(false);
        err = lowerMotor.setSmartCurrentLimit(lowerStallLimit, lowerFreeLimit, lowerLimitRPM);
        if (err != REVLibError.kOk)
            System.err.println("ERROR: " + err + " setting limits on lowerMotor");
        

        spinMotor = new WPI_TalonSRX(10);
        spinMotor.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        spinMotor.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        spinMotor.configContinuousCurrentLimit(continuousCurrentLimit, timeoutMs);
        spinMotor.enableCurrentLimit(true);
        
        //group = new SpeedControllerGroup(spinMotor);
        //addChild("SpinMotor", group);

        group1 = new SpeedControllerGroup(lowerMotor);
        addChild("LowerMotor", group1);

        SparkMaxLimitSwitch.Type open = SparkMaxLimitSwitch.Type.kNormallyOpen;

        limitUp = lowerMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        limitDown = lowerMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);


        upWidget = tab.add("Up Limit", false).withPosition(0, 2).getEntry();
        downWidget = tab.add("Down Limit", false).withPosition(0, 3).getEntry();

        lowerMotorSpeed = tab.add("lower motor speed", 0).withPosition(0, 0).getEntry();
        spinMotorSpeed = tab.add("spin motor speed", 0).withPosition(0, 1).getEntry();
    }

    /** Periodic update */
    @Override
    public void periodic() {

        boolean isUp = limitUp.isPressed();
        boolean isDown = limitDown.isPressed();

        long now = System.currentTimeMillis();
        if (now > dashTimeout) {

            upWidget.setBoolean(isUp);
            downWidget.setBoolean(isDown);

            lowerMotorSpeed.setNumber(lowerMotor.get());
            spinMotorSpeed.setNumber(spinMotor.get());
            dashTimeout = now + dashDelay;
        }
    }

    /** Start acquiring power cells */
    public void startAcquire() {
        lowerMotor.set(0.5);
        spinMotor.set(1.0);
    }

    /** Stop acquiring power cells */
    public void endAcquire() {
        lowerMotor.set(-0.5);
        spinMotor.set(0);
    }

    public void pause() {
        lowerMotor.set(0);
        spinMotor.set(0);
    }

    public void starReverse() {
        lowerMotor.set(0.0);
        spinMotor.set(-1.0);
    }
}
