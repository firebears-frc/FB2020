package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.firebears.Robot;
import org.firebears.commands.*;
import org.firebears.util.PIDSparkMotor;

public class Chassis extends SubsystemBase {

    private final Preferences config = Preferences.getInstance();

    private final CANSparkMax frontLeft;
    private final CANSparkMax rearLeft;
    private final CANSparkMax frontRight;
    private final CANSparkMax rearRight;
    private final PIDSparkMotor pidFrontLeft;
    private final PIDSparkMotor pidFrontRight;
    private final DifferentialDrive robotDrive;
    private final CANEncoder frontLeftEncoder;
    private final CANEncoder frontRightEncoder;
    private final long dashDelay;
    private long dashTimeout;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Chassis");
    private final NetworkTableEntry frontLeftTemp;
    private final NetworkTableEntry rearLeftTemp;
    private final NetworkTableEntry frontRightTemp;
    private final NetworkTableEntry rearRightTemp;

    private double direction = 1.0;
    private double pace = 1.0;

    public Chassis() {
        CANError err;
        int stallLimit = config.getInt("chassis.stallLimit", 65);
        int freeLimit = config.getInt("chassis.freeLimit", 20);
        int limitRPM = config.getInt("chassis.limitRPM", 1000);
        double kP = config.getDouble("chassis.p", 0.00015);
        double kI = config.getDouble("chassis.i", 0.0);
        double kD = config.getDouble("chassis.d", 0.0);
        double kP2 = config.getDouble("chassis.secondary.p", 0.015);
        double kI2 = config.getDouble("chassis.secondary.i", 0.0);
        double kD2 = config.getDouble("chassis.secondary.d", 0.0);
        boolean closedLoop = config.getBoolean("chassis.closedLoop", false);
        int chassisFrontLeftCanID = config.getInt("chassis.frontleft.canID", 4);
        int chassisRearLeftCanID = config.getInt("chassis.rearleft.canID", 5);
        int chassisFrontRightCanID = config.getInt("chassis.frontright.canID", 3);
        int chassisRearRightCanID = config.getInt("chassis.rearright.canID", 2);

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay;

        frontLeft = new CANSparkMax(chassisFrontLeftCanID, MotorType.kBrushless);
        frontLeft.setInverted(false);
        err = frontLeft.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on frontLeft");
        frontLeftEncoder = frontLeft.getEncoder();
        pidFrontLeft = new PIDSparkMotor(frontLeft, kP, kI, kD);
        pidFrontLeft.setClosedLoop(closedLoop);
        pidFrontLeft.setInvertEncoder(false);

        rearLeft = new CANSparkMax(chassisRearLeftCanID, MotorType.kBrushless);
        rearLeft.setInverted(false);
        err = rearLeft.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on rearLeft");
        rearLeft.follow(frontLeft);

        frontRight = new CANSparkMax(chassisFrontRightCanID, MotorType.kBrushless);
        frontRight.setInverted(false);
        err = frontRight.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on frontRight");
        frontRightEncoder = frontRight.getEncoder();

        pidFrontRight = new PIDSparkMotor(frontRight, kP, kI, kD);
        pidFrontRight.setClosedLoop(closedLoop);
        pidFrontRight.setInvertEncoder(true);
        pidFrontRight.setSecondaryPID(kP2, kI2, kD2);

        rearRight = new CANSparkMax(chassisRearRightCanID, MotorType.kBrushless);
        rearRight.setInverted(false);
        err = rearRight.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on rearRight");
        rearRight.follow(frontRight);

        robotDrive = new DifferentialDrive(pidFrontLeft, pidFrontRight);
        addChild("RobotDrive", robotDrive);

        setDefaultCommand(new DriveCommand(this));
        frontLeftTemp = tab.add("frontLeft temp", 0).withPosition(0, 0).getEntry();
        rearLeftTemp = tab.add("rearLeft temp", 0).withPosition(0, 1).getEntry();
        frontRightTemp = tab.add("frontRight temp", 0).withPosition(1, 0).getEntry();
        rearRightTemp = tab.add("rearRight temp", 0).withPosition(1, 1).getEntry();
    }

    @Override
    public void periodic() {
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            frontLeftTemp.setNumber(frontLeft.getMotorTemperature());
            rearLeftTemp.setNumber(rearLeft.getMotorTemperature());
            frontRightTemp.setNumber(frontRight.getMotorTemperature());
            rearRightTemp.setNumber(rearRight.getMotorTemperature());
            dashTimeout = now + dashDelay;
        }
        if (Math.abs(Robot.oi.xboxController.getTriggerAxis(Hand.kLeft)) > 0.5)
        {
            direction = -1;
        }

        if (Math.abs(Robot.oi.xboxController.getTriggerAxis(Hand.kRight)) > 0.5)
        {
            pace = 0.5;
        }
    }

    public double averageDistance() {
        double conversionFactor = config.getDouble("chassis.ticksToFeetConversionFactor", 0);
        return ((frontRightEncoder.getPosition() + frontLeftEncoder.getPosition()) * conversionFactor) / 2;
    }

    public double rotation() {
        double rotationConversionFactor = config.getDouble("chassis.ticksToDegreesConversionFactor", 0);
        return ((frontRightEncoder.getPosition() - frontLeftEncoder.getPosition()) * rotationConversionFactor);
    }

    public void drive(double speed, double rotation) {
        robotDrive.arcadeDrive(speed * direction * pace, rotation * direction * pace);
    }

   // public void invert() {
      //  direction *= -1;
   // }
}
