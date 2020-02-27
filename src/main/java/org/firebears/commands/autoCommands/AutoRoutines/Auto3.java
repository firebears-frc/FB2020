/* Auto that starts offset from port on non trench side backs up rotates and shoots */
package org.firebears.commands.autoCommands.AutoRoutines;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.firebears.commands.autoCommands.*;
import org.firebears.Robot;
import org.firebears.commands.*;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Auto3 extends SequentialCommandGroup {
  /**
   * Creates a new Auto3.
   */
  public Auto3() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new DriveStraightCommand(1.0),
    new TurnToAngleCommand(-45.0),
    new spinTHATwheel(Robot.shooter),
    new BallQueueCommand(Robot.storage),
    new BallQueueCommand(Robot.storage),
    new BallQueueCommand(Robot.storage),
    new ResetCommand(Robot.storage));
  }
}
