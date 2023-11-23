package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class RobotContainer {
    private static final class Constants {
        private static final int CONTROLLER_PORT = 0;

        public static final double JOYSTICK_DEADBAND = 0.05;

        private static final int PDP_CAN_ID = 1;
    }

    private final PowerDistribution pdp;

    private final Chassis chassis;
    private final Intake intake;
    private final Shooter shooter;

    private final CommandXboxController controller;

    public RobotContainer() {
        pdp = new PowerDistribution(Constants.PDP_CAN_ID, PowerDistribution.ModuleType.kCTRE);

        chassis = new Chassis();
        intake = new Intake();
        shooter = new Shooter();

        controller = new CommandXboxController(Constants.CONTROLLER_PORT);

        configureBindings();
    }

    private ChassisSpeeds getChassisSpeeds() {
        return new ChassisSpeeds(
                -MathUtil.applyDeadband(controller.getLeftY(), Constants.JOYSTICK_DEADBAND),
                0.0,
                -MathUtil.applyDeadband(controller.getLeftX(), Constants.JOYSTICK_DEADBAND));
    }

    private void configureBindings() {
        chassis.setDefaultCommand(chassis.defaultCommand(this::getChassisSpeeds));

        controller.a()
                .onTrue(shooter.shoot())
                .onFalse(shooter.stop());
        controller.b()
                .onTrue(shooter.reverse())
                .onFalse(shooter.stop());

        controller.leftTrigger()
                .onTrue(Commands.parallel(
                        intake.intake()))
                .onFalse(Commands.parallel(
                        intake.stop()));
        controller.leftBumper()
                .onTrue(Commands.parallel(
                        intake.reverse()))
                .onFalse(Commands.parallel(
                        intake.stop()));
    }
}
