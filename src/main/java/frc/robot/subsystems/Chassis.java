package frc.robot.subsystems;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.sparkmax.CurrentLimitConfiguration;
import frc.robot.util.sparkmax.SparkMaxConfiguration;
import frc.robot.util.sparkmax.StatusFrameConfiguration;

public class Chassis extends SubsystemBase {
    private static final class Constants {
        private static final int FRONT_LEFT_CAN_ID = 2;
        private static final int REAR_LEFT_CAN_ID = 3;
        private static final int FRONT_RIGHT_CAN_ID = 4;
        private static final int REAR_RIGHT_CAN_ID = 5;

        public static final SparkMaxConfiguration CONFIG = new SparkMaxConfiguration(
                false,
                IdleMode.kCoast,
                CurrentLimitConfiguration.complex(40, 20, 10, 60.0),
                StatusFrameConfiguration.normal());
    }

    private final CANSparkMax frontLeft, rearLeft, frontRight, rearRight;
    private final MotorControllerGroup left, right;
    private final DifferentialDrive drive;

    @AutoLogOutput(key = "Chassis/Target")
    private ChassisSpeeds targetSpeeds;

    public Chassis() {
        frontLeft = new CANSparkMax(Constants.FRONT_LEFT_CAN_ID, MotorType.kBrushless);
        rearLeft = new CANSparkMax(Constants.REAR_LEFT_CAN_ID, MotorType.kBrushless);
        frontRight = new CANSparkMax(Constants.FRONT_RIGHT_CAN_ID, MotorType.kBrushless);
        rearRight = new CANSparkMax(Constants.REAR_RIGHT_CAN_ID, MotorType.kBrushless);

        Constants.CONFIG.apply(frontLeft, rearLeft, frontRight, rearRight);

        left = new MotorControllerGroup(frontLeft, rearLeft);
        right = new MotorControllerGroup(frontRight, rearRight);
        drive = new DifferentialDrive(left, right);

        targetSpeeds = new ChassisSpeeds();
    }

    public Command defaultCommand(Supplier<ChassisSpeeds> supplier) {
        return run(() -> targetSpeeds = supplier.get());
    }

    @Override
    public void periodic() {
        drive.arcadeDrive(targetSpeeds.vxMetersPerSecond, targetSpeeds.omegaRadiansPerSecond, false);
    }
}
