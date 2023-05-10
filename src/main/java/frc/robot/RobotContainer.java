package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
    private final CommandJoystick joystick = new CommandJoystick(0);
    private final CommandXboxController controller = new CommandXboxController(1);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
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
