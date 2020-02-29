/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands.autoCommands;

import org.firebears.Robot;
import org.firebears.commands.spinTHATwheel;
import org.firebears.subsystems.Storage;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootAllCommand extends ParallelRaceGroup {
  /**
   * Creates a new AutonomousShootingCommand.
   */
  public ShootAllCommand() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new spinTHATwheel(Robot.shooter),
    new IndexAllCommand(Robot.storage));
  }
}
