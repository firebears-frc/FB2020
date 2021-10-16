package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.*;
import com.revrobotics.CANDigitalInput.LimitSwitch;
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

    private final CANDigitalInput limitUp;
    private final CANDigitalInput limitDown;

    //private final SpeedControllerGroup group;
    private final SpeedControllerGroup group1;

    /** Motor to lower acquisition system */
    private final CANSparkMax lowerMotor;
    private CANPIDController lowerMotorPidController;
    private final CANEncoder lowerMotorEncoder;

    /** Motor to spin the stars */
    private final WPI_TalonSRX spinMotor;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Acquisition");
    private final NetworkTableEntry lowerMotorSpeed;
    private final NetworkTableEntry lowerMotorPosition;
    private final NetworkTableEntry spinMotorSpeed;
    private final NetworkTableEntry upWidget;
    private final NetworkTableEntry downWidget;

    double lowerPID_P =  0.1;
    double lowerPID_I =  0.0;
    double lowerPID_D =  0.0;
    double lowerPID_FF = 0.0;

    private final long dashDelay;
    private long dashTimeout = 0;

    double lowerSetpoint_UP = 0.0;
    double lowerSetpoint_DOWN = 50.0;
    private double encoderHome = 0.0;


    public Acquisition() {
        CANError err;
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


        lowerPID_P = config.getDouble("acquisition.lowerMotor.pid.p", 0.1);
        lowerPID_I = config.getDouble("acquisition.lowerMotor.pid.i", 0.0);
        lowerPID_D = config.getDouble("acquisition.lowerMotor.pid.d", 0.0);
        lowerPID_FF = config.getDouble("acquisition.lowerMotor.pid.ff", 0.0);

        
        double lowerPID_maxSpeed = config.getDouble("acquisition.lowerMotor.pid.maxSpeed", 0.20);
        lowerSetpoint_UP = config.getDouble("acquisition.lowerMotor.pid.UP", 0.0);
        lowerSetpoint_DOWN = config.getDouble("acquisition.lowerMotor.pid.DOWN", 25.0);

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 150;

        lowerMotor = new CANSparkMax(acquisitionLowerMotorCanID, MotorType.kBrushless);
        lowerMotor.restoreFactoryDefaults();
        lowerMotor.setInverted(false);
        err = lowerMotor.setSmartCurrentLimit(lowerStallLimit, lowerFreeLimit, lowerLimitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on lowerMotor");
        lowerMotorEncoder = lowerMotor.getEncoder();
        /*
        lowerMotorPidController = lowerMotor.getPIDController();
        lowerMotorPidController.setP(lowerPID_P);
        lowerMotorPidController.setI(lowerPID_I);
        lowerMotorPidController.setD(lowerPID_D);
        lowerMotorPidController.setFF(lowerPID_FF);
        
        lowerMotorPidController.setSmartMotionMaxVelocity(30, 0);
        lowerMotorPidController.setSmartMotionMinOutputVelocity(0, 0);
        lowerMotorPidController.setSmartMotionMaxAccel(30, 0);
        lowerMotorPidController.setSmartMotionAllowedClosedLoopError(1, 0);

        */
       // lowerMotorPidController.setOutputRange(-1.0 * lowerPID_maxSpeed, lowerPID_maxSpeed);
        

        spinMotor = new WPI_TalonSRX(10);
        spinMotor.configPeakCurrentLimit(peakCurrentLimit, timeoutMs);
        spinMotor.configPeakCurrentDuration(peakCurrentDuration, timeoutMs);
        spinMotor.configContinuousCurrentLimit(continuousCurrentLimit, timeoutMs);
        spinMotor.enableCurrentLimit(true);
        
        //group = new SpeedControllerGroup(spinMotor);
        //addChild("SpinMotor", group);

        group1 = new SpeedControllerGroup(lowerMotor);
        addChild("LowerMotor", group1);

        limitDown = new CANDigitalInput(lowerMotor, CANDigitalInput.LimitSwitch.kForward, CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
        limitUp = new CANDigitalInput(lowerMotor, CANDigitalInput.LimitSwitch.kReverse, CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);


        upWidget = tab.add("Up Limit", false).withPosition(0, 2).getEntry();
        downWidget = tab.add("Down Limit", false).withPosition(0, 3).getEntry();

        lowerMotorSpeed = tab.add("lower motor speed", 0).withPosition(0, 0).getEntry();
        lowerMotorPosition = tab.add("lower motor position", 0).withPosition(0, 1).getEntry();
        spinMotorSpeed = tab.add("spin motor speed", 0).withPosition(0, 2).getEntry();
    }

    /** Periodic update */
    @Override
    public void periodic() {

        boolean isUp = limitUp.get();
        boolean isDown = limitDown.get();

        long now = System.currentTimeMillis();
        if (now > dashTimeout) {

            upWidget.setBoolean(isUp);
            downWidget.setBoolean(isDown);

            lowerMotorSpeed.setNumber(lowerMotor.get());
            lowerMotorPosition.setNumber(lowerMotorEncoder.getPosition() - encoderHome);
            spinMotorSpeed.setNumber(spinMotor.get());
            dashTimeout = now + dashDelay;
        }

        if (isUp) {
            encoderHome = lowerMotorEncoder.getPosition();
        }
    }

    /* Start acquiring power cells */
    public void startAcquire() {
        
        disablePID();
        lowerMotor.set(0.1);
        //lowerMotorPidController.setReference(lowerSetpoint_DOWN + encoderHome, ControlType.kPosition);
        spinMotor.set(1.0);
    }

    /* Stop acquiring power cells */
    public void endAcquire() {
        disablePID();
        lowerMotor.set(-0.2);
        //lowerMotorPidController.setReference(lowerSetpoint_UP + encoderHome, ControlType.kPosition);
        spinMotor.set(0);
    }

    public void pause() {
        lowerMotor.set(0);
        spinMotor.set(0);
       // enablePID();
    }

    
    public void pauseDown() {
        lowerMotor.set(0);
    }

    public void starReverse() {
        lowerMotor.set(0.0);
        spinMotor.set(-1.0);
    }

    public boolean isUp() {
        return limitUp.get();
    }

    public boolean isDown() {
        return limitDown.get() || lowerMotorEncoder.getPosition() >= (lowerSetpoint_DOWN+encoderHome);
    }

    public void enablePID(){
        /*
        lowerMotorPidController.setP(lowerPID_P);
        lowerMotorPidController.setI(lowerPID_I);
        lowerMotorPidController.setD(lowerPID_D);
        lowerMotorPidController.setFF(lowerPID_FF);
        */
    }

    public void disablePID(){
        /*
        lowerMotorPidController.setP(0.0);
        lowerMotorPidController.setI(0.0);
        lowerMotorPidController.setD(0.0);
        lowerMotorPidController.setFF(0.0);
        */
    }
}
