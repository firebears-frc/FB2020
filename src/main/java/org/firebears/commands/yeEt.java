package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import org.firebears.subsystems.Shooter;
import org.firebears.subsystems.Storage;

public class yeEt extends ParallelRaceGroup {
    public yeEt(Storage storage, Shooter shooter) {
        super(
            new ShooterSpinUp(shooter),
            new IndexShootingCommand(storage, shooter)
        );
    }
}