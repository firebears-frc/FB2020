//Starting position 15 Roberts from our trench wall to closest robot edge

package org.firebears.commands.autoCommands.AutoRoutines;
import org.firebears.commands.autoCommands.*;
import org.firebears.subsystems.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MiddleAuto extends SequentialCommandGroup {
  /**
   * Creates a new MiddleAuto.
   */
  public MiddleAuto(Chassis chassis, Shooter shooter) {
    addCommands(
      new DriveStraightCommand(1.0),
      new TurnToAngleCommand(90.0),
      new DriveStraightCommand(10),
      new TurnToAngleCommand(0.0)
      //add shoot command here
    );
  }
}
