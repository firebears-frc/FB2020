package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.firebears.commands.*;
import org.firebears.util.PIDSparkMotor;

public class Chassis extends SubsystemBase {

    private final CANSparkMax rearRight;
    private final CANSparkMax frontRight;
    private final CANSparkMax frontLeft;
    private final CANSparkMax rearLeft;
    public final PIDSparkMotor pidFrontRight;
    public final PIDSparkMotor pidFrontLeft;
    private final DifferentialDrive robotDrive;
    private final CANEncoder frontRightEncoder;
    private final CANEncoder frontLeftEncoder;

    private final Preferences config = Preferences.getInstance();

    public Chassis() {
        double kP = config.getDouble("chassis.p", 0.00015);
        double kI = config.getDouble("chassis.i", 0.0);
        double kD = config.getDouble("chassis.d", 0.0);
        double kP2 = config.getDouble("chassis.secondary.p", 0.015);
        double kI2 = config.getDouble("chassis.secondary.i", 0.0);
        double kD2 = config.getDouble("chassis.secondary.d", 0.0);
        boolean closedLoop = config.getBoolean("chassis.closedLoop", false);
        // navXUsePitchAngle = config.getBoolean("chassis.navXUsePitchAngle", true);
        // navXPitchOffset = config.getDouble("chassis.navXPitchOffset", 5.0);

        int chassisRearRightCanID = config.getInt("chassis.rearright.canID", 2);
        rearRight = new CANSparkMax(chassisRearRightCanID, MotorType.kBrushless);
        rearRight.setInverted(false);

        int chassisFrontRightCanID = config.getInt("chassis.frontright.canID", 3);
        frontRight = new CANSparkMax(chassisFrontRightCanID, MotorType.kBrushless);
        frontRight.setInverted(false);
        frontRightEncoder = frontRight.getEncoder();
        pidFrontRight = new PIDSparkMotor(frontRight, kP, kI, kD);
        pidFrontRight.setClosedLoop(closedLoop);
        pidFrontRight.setInvertEncoder(true);
        pidFrontRight.setSecondaryPID(kP2, kI2, kD2);

        rearRight.follow(frontRight);

        int chassisFrontLeftCanID = config.getInt("chassis.frontleft.canID", 4);
        frontLeft = new CANSparkMax(chassisFrontLeftCanID, MotorType.kBrushless);
        frontLeft.setInverted(false);
        frontLeftEncoder = frontLeft.getEncoder();
        pidFrontLeft = new PIDSparkMotor(frontLeft, kP, kI, kD);
        pidFrontLeft.setClosedLoop(closedLoop);

        int chassisRearLeftCanID = config.getInt("chassis.rearleft.canID", 5);
        rearLeft = new CANSparkMax(chassisRearLeftCanID, MotorType.kBrushless);
        rearLeft.setInverted(false);

        rearLeft.follow(frontLeft);

        robotDrive = new DifferentialDrive(pidFrontLeft, pidFrontRight);
        addChild("RobotDrive", robotDrive);

        setDefaultCommand(new DriveCommand(this));
    }

    @Override
    public void periodic() {

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
        robotDrive.arcadeDrive(speed, rotation);
    }
}