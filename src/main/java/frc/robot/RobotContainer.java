// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AcquisitionStart;
import frc.robot.commands.AcquisitionStop;
import frc.robot.commands.StorageAdvanceCommand;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final Storage m_storageSubsystem = new Storage();
  private final Shooter m_shooterSubsystem = new Shooter();
  private final Loader m_loaderSubsystem = new Loader();
  private final Acquisition m_acquisitionSubsystem = new Acquisition();
  private final Drivetrain m_Drivetrain = new Drivetrain();

  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  public RobotContainer() {
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_driverController.a()
      .onTrue(new StorageAdvanceCommand(m_storageSubsystem));

    m_driverController.x()
      .onTrue(new InstantCommand(m_shooterSubsystem::shootSpeed, m_shooterSubsystem));

    m_driverController.y()
      .onTrue(new InstantCommand(m_shooterSubsystem::idleSpeed, m_shooterSubsystem));
    
    m_driverController.b()
      .onTrue(new InstantCommand(m_shooterSubsystem::stop, m_shooterSubsystem));

    m_driverController.rightTrigger()
      .onTrue(new AcquisitionStart(m_acquisitionSubsystem, m_loaderSubsystem));

    m_driverController.leftTrigger()
      .onTrue(new AcquisitionStop(m_acquisitionSubsystem, m_loaderSubsystem));

    m_driverController.leftBumper()
      .onTrue(new InstantCommand(m_loaderSubsystem::reverse, m_loaderSubsystem));
    
    m_driverController.rightBumper()
      .onTrue(new InstantCommand(m_acquisitionSubsystem::reverseAcquire, m_acquisitionSubsystem));

    m_Drivetrain.setDefaultCommand(m_Drivetrain.defaultCommand(m_driverController::getLeftY, m_driverController::getLeftX));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return new WaitCommand(1);
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
