/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands.autoCommands;

import org.firebears.Robot;
import org.firebears.subsystems.Chassis;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html


public class DriveStraightCommand extends PIDCommand {

  double targetDistance;

  public DriveStraightCommand(double distance) {
    super(
        // The controller that the command will use
        new PIDController(0.3, 0.15, 0),
        // This should return the measurement
        () -> Robot.chassis.getAverageDistance(),
        // This should return the setpoint (can also be a constant)
        () -> distance,
        // This uses the output
        output -> {
          Robot.chassis.drive(0, output); 
        });
    addRequirements(Robot.chassis);
    targetDistance = distance;
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double distanceDiff = Math.abs(Robot.chassis.getAverageDistance() - targetDistance);
    return (distanceDiff < .3);
  }    

  @Override
  public void end(boolean interrupted) {
    Robot.chassis.drive(0, 0);
  }

}
