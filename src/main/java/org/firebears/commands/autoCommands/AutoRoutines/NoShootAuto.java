/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands.autoCommands.AutoRoutines;
import org.firebears.commands.autoCommands.*;
import org.firebears.subsystems.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class NoShootAuto extends SequentialCommandGroup {
  /**
   * Creates a new NoShootAuto.
   */
  public NoShootAuto(Chassis chassis, Shooter shooter) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    addCommands(
      new DriveStraightCommand(-1.0)
      //add shoot command here
    );
  }
}
