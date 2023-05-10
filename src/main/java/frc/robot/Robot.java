package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    private Command autoCommand;
    private RobotContainer container;

    @Override
    public void robotInit() {
        container = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        autoCommand = container.getAutonomousCommand();

        if (autoCommand != null) {
            autoCommand.schedule();
        }
    }

    @Override
    public void teleopInit() {
        if (autoCommand != null) {
            autoCommand.cancel();
            autoCommand = null;
        }
    }
}
