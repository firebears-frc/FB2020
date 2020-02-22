package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.firebears.subsystems.Shooter;
import org.firebears.subsystems.Storage;

public class yeEt extends ParallelCommandGroup {
    public yeEt(Storage storage, Shooter shooter) {
        super(new spinTHATwheel(shooter),
            new IndexShootingCommand(storage, shooter));
    }
}