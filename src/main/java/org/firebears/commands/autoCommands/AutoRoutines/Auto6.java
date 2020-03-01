/* Auto that starts by trench wall and picks up 2 balls from trench instead of shooting*/
package org.firebears.commands.autoCommands.AutoRoutines;

import org.firebears.commands.AcquisitionLowerCommand;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.firebears.commands.autoCommands.*;
import org.firebears.Robot;
import org.firebears.commands.*;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Auto6 extends ParallelCommandGroup {
  /**
   * Creates a new Auto6.
   */
  public Auto6() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());super();
    super(new AcquisitionLowerCommand(Robot.acquisition, Robot.loader, Robot.storage),
    new ResetEncoderCommand(),
    new DriveStraightCommand(7.0));
  }
}
