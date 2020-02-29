/* Auto that starts lined up from port backs up and shoots */
package org.firebears.commands.autoCommands.AutoRoutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.firebears.commands.autoCommands.*;
import org.firebears.subsystems.*;
import org.firebears.Robot;
import org.firebears.commands.*;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Auto2 extends SequentialCommandGroup {
  /**
   * Creates a new Auto2.
   */
  public Auto2(Chassis chassis, Storage storage, Shooter shooter) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new ShootAllCommand(),
   // new ResetEncoderCommand(),
    new DriveStraightCommand(3.0));
    //new ResetCommand(Robot.storage));
  }
}
