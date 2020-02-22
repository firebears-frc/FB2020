package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.subsystems.Chassis;
import org.firebears.subsystems.Vision;

public class TurnToTargetCommand extends CommandBase {

    private final Chassis chassis;
    private final Vision vision;

    float marginOfError = 2;

    public TurnToTargetCommand(Chassis chassis, Vision vision) {
        this.chassis = chassis;
        this.vision = vision;
        addRequirements(chassis);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (vision.getTargetConfidenceBoolean()) {
            double rotation = 1.0 / 10.0 * vision.getTargetAngleX();
            chassis.drive(0, rotation);
        } else {
            chassis.drive(0, 0);
        }
    }

    @Override
    public boolean isFinished() {
        return Math.abs(vision.getTargetAngleX()) < marginOfError;
    }

    @Override
    public void end(boolean interrupted) {
    }
}