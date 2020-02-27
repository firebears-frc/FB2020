package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Shooter;

public class ShooterSpinUp extends CommandBase {

    private final Shooter shooter;

    /**
     * Creates a new ShooterSpinUp.
     */
    public ShooterSpinUp(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        shooter.spinUp();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}