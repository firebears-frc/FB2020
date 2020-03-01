/* Auto that starts in the middle of initiation line drives to line up with port and shoots */
package org.firebears.commands.autoCommands.AutoRoutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.firebears.commands.autoCommands.*;
import org.firebears.Robot;
import org.firebears.commands.*;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Auto4 extends SequentialCommandGroup {
  /**
   * Creates a new Auto4.
   */
  public Auto4() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new DriveStraightCommand(1.0),
    new ResetEncoderCommand(),
    new TurnToAngleCommand(90.0),
    new ResetEncoderCommand(),
    new DriveStraightCommand(6.0),
    new ShootAllCommand(),
    new ResetCommand(Robot.storage));
    
  }
}
