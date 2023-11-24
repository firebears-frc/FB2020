package frc.robot.subsystems;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

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

    private final CANSparkMax frontLeft, rearLeft, frontRight, rearRight;
    private final MotorControllerGroup left, right;
    private final DifferentialDrive drive;

    @AutoLogOutput(key = "Chassis/Target")
    private ChassisSpeeds targetSpeeds;

    public Chassis() {
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
        frontRight.setInverted(false);
        frontRight.setIdleMode(IdleMode.kCoast);
        frontRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        frontRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        rearRight = new CANSparkMax(Constants.REAR_RIGHT_CAN_ID, MotorType.kBrushless);
        rearRight.restoreFactoryDefaults();
        rearRight.setInverted(false);
        rearRight.setIdleMode(IdleMode.kCoast);
        rearRight.setSmartCurrentLimit(Constants.STALL_CURRENT_LIMIT, Constants.FREE_CURRENT_LIMIT);
        rearRight.setSecondaryCurrentLimit(Constants.SECONDARY_CURRENT_LIMIT);

        left = new MotorControllerGroup(frontLeft, rearLeft);
        right = new MotorControllerGroup(frontRight, rearRight);
        drive = new DifferentialDrive(left, right);

        frontLeft.burnFlash();
        rearLeft.burnFlash();
        frontRight.burnFlash();
        rearRight.burnFlash();

        // https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        frontLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        rearLeft.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        frontRight.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 1000);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 1000);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 1000);
        rearRight.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 1000);

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
