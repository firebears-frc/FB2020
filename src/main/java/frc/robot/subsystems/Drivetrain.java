package frc.robot.subsystems;

import java.util.function.Supplier;

import org.opencv.features2d.FlannBasedMatcher;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    private static class Constants {
        public static final int FRONT_LEFT_CAN_ID = 2;
        public static final int REAR_LEFT_CAN_ID = 3;
        public static final int FRONT_RIGHT_CAN_ID = 4;
        public static final int REAR_RIGHT_CAN_ID = 5;

        public static final int STALL_CURRENT_LIMIT = 30;
        public static final int FREE_CURRENT_LIMIT = 20;
        public static final double SECONDARY_CURRENT_LIMIT = 60.0;
    }

    private final CANSparkMax frontLeft, frontRight, rearLeft, rearRight;
    private final MotorControllerGroup left, right;
    private final DifferentialDrive drivetrain;

    public Drivetrain() {
        frontLeft = new CANSparkMax(Constants.FRONT_LEFT_CAN_ID, MotorType.kBrushless);
        frontLeft.restoreFactoryDefaults();
        frontLeft.setInverted(false);
        frontLeft.setIdleMode(IdleMode.kCoast);
        frontLeft.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        frontLeft.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);
        rearLeft = new CANSparkMax(Constants.REAR_LEFT_CAN_ID, MotorType.kBrushless);
        rearLeft.restoreFactoryDefaults();
        rearLeft.setInverted(false);
        rearLeft.setIdleMode(IdleMode.kCoast);
        rearLeft.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        rearLeft.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        frontRight = new CANSparkMax(Constants.FRONT_RIGHT_CAN_ID, MotorType.kBrushless);
        frontRight.restoreFactoryDefaults();
        frontRight.setInverted(true);
        frontRight.setIdleMode(IdleMode.kCoast);
        frontRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        frontRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);
        rearRight = new CANSparkMax(Constants.REAR_RIGHT_CAN_ID, MotorType.kBrushless);
        rearRight.restoreFactoryDefaults();
        rearRight.setInverted(true);
        rearRight.setIdleMode(IdleMode.kCoast);
        rearRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        rearRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        left = new MotorControllerGroup(frontLeft, rearLeft);
        right = new MotorControllerGroup(frontRight, rearRight);

        drivetrain = new DifferentialDrive(left, right);

        frontLeft.burnFlash();
        rearLeft.burnFlash();
        frontRight.burnFlash();
        rearRight.burnFlash();
    }

    private void drive(double forward, double rotation) {
        drivetrain.arcadeDrive(forward, rotation);
        System.out.println("71");
    }

    public Command defaultCommand(Supplier<Double> forwardSupplier, Supplier<Double> rotationSupplier) {
        return new RunCommand(() -> {
            System.out.println("76");
            double forward = forwardSupplier.get() * -1.0;
            double rotation = rotationSupplier.get() * -1.0;

            drive(forward, rotation);
        }, this);
    }
}