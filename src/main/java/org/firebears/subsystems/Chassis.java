package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.BoardAxis;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.firebears.Robot;
import org.firebears.util.PIDSparkMotor;

public class Chassis extends SubsystemBase {

    /** Maximum acceleration per 20 ms tick */
    static private final double MAX_ACCEL = 0.1;

    /** Maximum deceleration per 20 ms tick */
    static private final double MAX_DECEL = 0.2;

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
    private final double ticksPerRobert;
    private final long dashDelay;
    private long dashTimeout = 0;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Chassis");
    private final NetworkTableEntry frontLeftTemp;
    private final NetworkTableEntry rearLeftTemp;
    private final NetworkTableEntry frontRightTemp;
    private final NetworkTableEntry rearRightTemp;
    private final NetworkTableEntry speedWidget;
    private final NetworkTableEntry rotationWidget;
    private final NetworkTableEntry distanceWidget;

    private final NetworkTableEntry xAxis;
    private final NetworkTableEntry yAxis;
    private final NetworkTableEntry zAxis;


    private double direction = 1.0;
    private double pace = 1.0;
    private double speed = 0.0;
    private double rotation = 0.0;

    AHRS navXboard;

    public Chassis() {
        CANError err;
        final int stallLimit = config.getInt("chassis.stallLimit", 65);
        final int freeLimit = config.getInt("chassis.freeLimit", 20);
        final int limitRPM = config.getInt("chassis.limitRPM", 1000);
        final double kP = config.getDouble("chassis.p", 0.00015);
        final double kI = config.getDouble("chassis.i", 0.0);
        final double kD = config.getDouble("chassis.d", 0.0);
        final double kP2 = config.getDouble("chassis.secondary.p", 0.015);
        final double kI2 = config.getDouble("chassis.secondary.i", 0.0);
        final double kD2 = config.getDouble("chassis.secondary.d", 0.0);
        final boolean closedLoop = config.getBoolean("chassis.closedLoop", false);
        final int chassisFrontLeftCanID = config.getInt("chassis.frontleft.canID", 2);
        final int chassisRearLeftCanID = config.getInt("chassis.rearleft.canID", 3);
        final int chassisFrontRightCanID = config.getInt("chassis.frontright.canID", 4);
        final int chassisRearRightCanID = config.getInt("chassis.rearright.canID", 5);

        ticksPerRobert = config.getDouble("chassis.ticksPerRobert", 5.45);

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

        distanceWidget = tab.add("Distance traveled", 0).withPosition(3, 2).getEntry();

        

        frontLeftTemp = tab.add("frontLeft temp", 0).withPosition(0, 0).getEntry();
        rearLeftTemp = tab.add("rearLeft temp", 0).withPosition(0, 1).getEntry();
        frontRightTemp = tab.add("frontRight temp", 0).withPosition(1, 0).getEntry();
        rearRightTemp = tab.add("rearRight temp", 0).withPosition(1, 1).getEntry();
        speedWidget = tab.add("speed", 0).withPosition(2, 0).getEntry();
        rotationWidget = tab.add("rotation", 0).withPosition(2, 1).getEntry();

        try {
            navXboard = new AHRS(edu.wpi.first.wpilibj.SerialPort.Port.kUSB);
        } catch (final RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }

        xAxis = tab.add("Angle", 0).withPosition(3, 0).getEntry();
        yAxis = tab.add("Pitch", 0).withPosition(3, 2).getEntry();
        zAxis = tab.add("Roll", 0).withPosition(3, 2).getEntry();
        
        resetEncoders();

    }

    @Override
    public void periodic() {
        final long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            frontLeftTemp.setNumber(frontLeft.getMotorTemperature());
            rearLeftTemp.setNumber(rearLeft.getMotorTemperature());
            frontRightTemp.setNumber(frontRight.getMotorTemperature());
            rearRightTemp.setNumber(rearRight.getMotorTemperature());
            speedWidget.setNumber(getSpeed());
            rotationWidget.setNumber(getRotation());
            distanceWidget.setNumber(getAverageDistance());

            zAxis.setNumber(navXboard.getRoll());
            xAxis.setNumber(navXboard.getAngle());
            yAxis.setNumber(navXboard.getPitch());

            dashTimeout = now + dashDelay;
        }
        direction = getDirection();
        pace = getPace();
    }

    /** Get selected direction */
    private double getDirection() {
        if (Math.abs(Robot.oi.xboxController.getTriggerAxis(Hand.kLeft)) < 0.5)
            return 1.0;
        else
            return -1.0;
    }

    /** Get selected pace */
    private double getPace() {
        if (Math.abs(Robot.oi.xboxController.getTriggerAxis(Hand.kRight)) < 0.5)
            return 1.0;
        else
            return 0.5;
    }

    /** Get left distance in ticks */
    private double ticksLeft() {
        return frontLeftEncoder.getPosition();
    }

    /** Get right distance in ticks */
    private double ticksRight() {
        return frontRightEncoder.getPosition();
    }

    public double rotation() {
        final double rotationConversionFactor = config.getDouble("chassis.ticksToDegreesConversionFactor", 0);
        return (ticksRight() - ticksLeft()) * rotationConversionFactor;
    }

    public void drive(final double speed, final double rotation) {
        this.speed = filterSpeed(speed);
        this.rotation = filterRotation(rotation);
        robotDrive.arcadeDrive(getSpeed(), getRotation());
    }

    private double filterSpeed(double s) {
        s = Math.min(s, speed + MAX_ACCEL);
        s = Math.max(s, speed - MAX_DECEL);
        return s;
    }

    private double filterRotation(double r) {
        final double accel = Math.max(speed, rotation);
        r = Math.min(r, accel + MAX_ACCEL);
        final double decel = Math.min(speed, rotation);
        r = Math.max(r, decel - MAX_DECEL);
        return r;
    }

    private double getSpeed() {
        return speed * direction * pace;
    }

    private double getRotation() {
        return rotation * pace;
    }

    private double getAverageEncoderTicks() {
        final double ticks = (Math.abs(frontLeftEncoder.getPosition()) + Math.abs(frontRightEncoder.getPosition())) / 2;
        if (frontLeftEncoder.getPosition() >= 0) {
            return ticks;
        } else {
            return ticks * -1;
        }
    }

    public double getAverageDistance() {
        return (getAverageEncoderTicks() / ticksPerRobert);
    }

    public void resetEncoders() {
        frontLeftEncoder.setPosition(0.0);
        frontRightEncoder.setPosition(0.0);
    }

    public void setBrake(final boolean EnableBrake) {
        if(EnableBrake){
            frontLeft.setIdleMode(IdleMode.kBrake);
            frontRight.setIdleMode(IdleMode.kBrake);
            rearLeft.setIdleMode(IdleMode.kBrake);
            rearRight.setIdleMode(IdleMode.kBrake);
        } else{
            frontLeft.setIdleMode(IdleMode.kCoast);
            frontRight.setIdleMode(IdleMode.kCoast);
            rearLeft.setIdleMode(IdleMode.kCoast);
            rearRight.setIdleMode(IdleMode.kCoast);
        }

        
    }

    public double getAngle() {
        if (navXboard != null){
            return navXboard.getAngle();
        } else {
            return -1000.0;
        }


        
    }
}
