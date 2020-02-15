package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.Robot;
import org.firebears.subsystems.Chassis;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnToTargetCommand extends CommandBase {

    private final Preferences config = Preferences.getInstance();
    float marginOfError = 2;

    public TurnToTargetCommand(Chassis chassis) {
        addRequirements(chassis);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        if (Robot.vision.getTargetConfidenceBoolean()) {
            double rotation = 1.0 / 10.0 * Robot.vision.getTargetAngleX();
            Robot.chassis.drive(0, rotation);
            SmartDashboard.putNumber("rotation", rotation);
        } else {
            Robot.chassis.drive(0, 0);
            SmartDashboard.putNumber("rotation", 0);
        }
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(Robot.vision.getTargetAngleX()) < marginOfError){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
    }
}
