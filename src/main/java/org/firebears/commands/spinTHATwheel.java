package org.firebears.commands;

import org.firebears.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class spinTHATwheel extends CommandBase {
  /**
   * Creates a new spinTHATwheel.
   */
  public spinTHATwheel() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.shooter.setTargetRPM(1000.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Robot.shooter.isWheelSpunUp();
  }
}
