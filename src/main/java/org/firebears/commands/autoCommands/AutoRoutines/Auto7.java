/* Auto that can start anywhere only drives off line slightly*/
package org.firebears.commands.autoCommands.AutoRoutines;
import org.firebears.commands.autoCommands.*;
import org.firebears.subsystems.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Auto7 extends SequentialCommandGroup {

  public Auto7(Chassis chassis) {
      super(new ResetEncoderCommand(),
      new DriveStraightCommand(3.0));
    
  }
}

