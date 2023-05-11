package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class RobotContainer {
    public class Constants {
        public static final int CONTROLLER_PORT = 0;
    }

    private final Chassis chassis;
    private final Shooter shooter;
    private final Intake intake;
    private final Feeder feeder;
    private final Indexer indexer;
    private final CommandXboxController controller;

    public RobotContainer() {
        chassis = new Chassis();
        shooter = new Shooter();
        intake = new Intake();
        feeder = new Feeder();
        indexer = new Indexer();
        controller = new CommandXboxController(Constants.CONTROLLER_PORT);

        configureBindings();
        displayGitInfo();
    }

    private void configureBindings() {
        // Chassis
        chassis.setDefaultCommand(chassis.defaultCommand(controller::getLeftY, controller::getLeftX));

        // Intake & Feeder
        controller.leftBumper().whileTrue(intake.intake()).whileTrue(feeder.run());
        controller.b().whileTrue(intake.reverse()).whileTrue(feeder.reverse());

        // Indexer
        controller.a().whileTrue(indexer.run());
        controller.y().whileTrue(indexer.reverse());

        // Shooter
        controller.rightBumper().whileTrue(shooter.shoot());
        controller.x().whileTrue(shooter.reverse());
    }

    public Command getAutonomousCommand() {
        return null;
    }

    /**
     * Display the git info on the dashboard via network tables
     */
    private void displayGitInfo() {
        final NetworkTable table = NetworkTableInstance.getDefault().getTable("Build Info");
        table.getEntry("Branch Name").setValue(BuildConstants.GIT_BRANCH);
        table.getEntry("Commit Hash (Short)").setValue(BuildConstants.GIT_SHA.substring(0, 8));
        table.getEntry("Commit Hash (Full)").setValue(BuildConstants.GIT_SHA);
        table.getEntry("Build Time").setValue(BuildConstants.BUILD_DATE);
    }
}
