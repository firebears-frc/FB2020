//Starting position 5 Roberts from our trench wall to closest robot edge

package org.firebears.commands.autoCommands.AutoRoutines;

import org.firebears.commands.autoCommands.*;
import org.firebears.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ClosestAuto extends SequentialCommandGroup {
  /**
   * Creates a new ClosestAuto.
   */
  public ClosestAuto(Chassis chassis, Shooter shooter) {
    addCommands(
      new DriveStraightCommand(-1.0)
      //add shoot command here
    );
  }
}
