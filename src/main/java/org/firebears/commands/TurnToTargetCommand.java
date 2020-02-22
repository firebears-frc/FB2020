package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.Robot;
import org.firebears.subsystems.Chassis;

public class TurnToTargetCommand extends CommandBase {

    private final Chassis chassis;

    float marginOfError = 2;

    public TurnToTargetCommand(Chassis chassis) {
        this.chassis = chassis;
        addRequirements(chassis);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (Robot.vision.getTargetConfidenceBoolean()) {
            double rotation = 1.0 / 10.0 * Robot.vision.getTargetAngleX();
            chassis.drive(0, rotation);
        } else {
            chassis.drive(0, 0);
        }
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Robot.vision.getTargetAngleX()) < marginOfError;
    }

    @Override
    public void end(boolean interrupted) {
    }
}