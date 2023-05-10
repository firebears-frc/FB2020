package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Chassis;

public class RobotContainer {
    public class Constants {
        public static final int JOYSTICK_PORT = 0;
        public static final int CONTROLLER_PORT = 1;
    }

    private final Chassis chassis;
    private final CommandJoystick joystick;
    private final CommandXboxController controller;

    public RobotContainer() {
        chassis = new Chassis();
        joystick = new CommandJoystick(Constants.JOYSTICK_PORT);
        controller = new CommandXboxController(Constants.CONTROLLER_PORT);

        configureBindings();
    }

    private void configureBindings() {
        chassis.setDefaultCommand(chassis.defaultCommand(joystick::getY, joystick::getX));
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
