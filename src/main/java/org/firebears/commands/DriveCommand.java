package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.firebears.Robot;
import org.firebears.subsystems.Chassis;

import edu.wpi.first.wpilibj.Preferences;

/**
 *
 */
public class DriveCommand extends CommandBase {
  final Preferences config;
  int joystickSpeedAxis;
  int joystickRotateAxis;
  double adjust;

  public DriveCommand(Chassis chassis) {
    addRequirements(chassis);
    config = Preferences.getInstance();
    joystickSpeedAxis = config.getInt("joystick1.speedAxis", 1);
    joystickRotateAxis = config.getInt("joystick1.rotateAxis", 4);
    adjust = config.getDouble("driveCommand.deadBand", 0.1);

  }

  
  @Override
  public void initialize() {

  }

  
  @Override
  public void execute() {
    double speed = -1 * Robot.oi.getXboxController().getRawAxis(joystickSpeedAxis);

    double rotation = Robot.oi.getXboxController().getRawAxis(joystickRotateAxis) * 0.7;
    Robot.chassis.drive((speed), (rotation));
  }

  
  @Override
  public boolean isFinished() {
    return false;
  }

  
  @Override
  public void end(boolean interrupted) {
  }

}
