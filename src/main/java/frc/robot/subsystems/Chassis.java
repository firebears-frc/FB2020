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

public class Chassis extends SubsystemBase {
    private static final class Constants {
        private static final int FRONT_LEFT_CAN_ID = 2;
        private static final int REAR_LEFT_CAN_ID = 3;
        private static final int FRONT_RIGHT_CAN_ID = 4;
        private static final int REAR_RIGHT_CAN_ID = 5;

        public static final int STALL_CURRENT_LIMIT = 40;
        public static final int FREE_CURRENT_LIMIT = 20;
        public static final double SECONDARY_CURRENT_LIMIT = 60.0;
    }

    private final CANSparkMax frontLeft = new CANSparkMax(Constants.FRONT_LEFT_CAN_ID, MotorType.kBrushless);
    private final CANSparkMax rearLeft = new CANSparkMax(Constants.REAR_LEFT_CAN_ID, MotorType.kBrushless);
    private final CANSparkMax frontRight = new CANSparkMax(Constants.FRONT_RIGHT_CAN_ID, MotorType.kBrushless);
    private final CANSparkMax rearRight = new CANSparkMax(Constants.REAR_RIGHT_CAN_ID, MotorType.kBrushless);
    private final MotorControllerGroup left = new MotorControllerGroup(frontLeft, rearLeft);
    private final MotorControllerGroup right = new MotorControllerGroup(frontRight, rearRight);
    private final DifferentialDrive drive = new DifferentialDrive(left, right);

    private ChassisSpeeds target = new ChassisSpeeds();

    public Chassis() {
        frontLeft.restoreFactoryDefaults();
        frontLeft.setInverted(false);
        frontLeft.setIdleMode(IdleMode.kCoast);
        frontLeft.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        frontLeft.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        rearLeft.restoreFactoryDefaults();
        rearLeft.setInverted(false);
        rearLeft.setIdleMode(IdleMode.kCoast);
        rearLeft.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        rearLeft.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        frontRight.restoreFactoryDefaults();
        frontRight.setInverted(false);
        frontRight.setIdleMode(IdleMode.kCoast);
        frontRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        frontRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        rearRight.restoreFactoryDefaults();
        rearRight.setInverted(false);
        rearRight.setIdleMode(IdleMode.kCoast);
        rearRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        rearRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        frontLeft.burnFlash();
        rearLeft.burnFlash();
        frontRight.burnFlash();
        rearRight.burnFlash();
    }

    private void drive(ChassisSpeeds speeds) {
        target = speeds;

        drive.arcadeDrive(speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond, false);
    }

    @AutoLogOutput
    public ChassisSpeeds targetSpeeds() {
        return target;
    }

    public Command defaultCommand(Supplier<ChassisSpeeds> supplier) {
        return run(() -> drive(supplier.get()));
    }
}

