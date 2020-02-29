/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.firebears.commands.autoCommands;

import org.firebears.Robot;
import org.firebears.commands.BallQueueCommand;
import org.firebears.subsystems.Storage;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class IndexAllCommand extends SequentialCommandGroup {
  /**
   * Creates a new Shoot5Command.
   */
    static double waitTime = 0.5;
    static double startWaitTime = 1.0;

  public IndexAllCommand(Storage storage) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new WaitCommand(startWaitTime),
    new BallQueueCommand(storage),
    new WaitCommand(waitTime),
    new BallQueueCommand(storage),
    new WaitCommand(waitTime),
    new BallQueueCommand(storage),
    new WaitCommand(waitTime),
    new BallQueueCommand(storage),
    new WaitCommand(waitTime),
    new BallQueueCommand(storage));
    }
}
