package org.firebears.subsystems;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSparkMAX extends SubsystemBase implements Climber {

    private final Preferences config = Preferences.getInstance();
    private final CANSparkMax motor;
    
    public ClimberSparkMAX(String configCANID, int defCANID) {
        CANError err;
        int stallLimit = config.getInt("climber.stallLimit", 60);
        int freeLimit = config.getInt("climber.freeLimit", 20);
        int limitRPM = config.getInt("climber.limitRPM", 1000);
        int canID = config.getInt(configCANID, defCANID);

        motor = new CANSparkMax(canID, MotorType.kBrushless);

        motor.setInverted(false);
        err = motor.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on climber");

        SpeedControllerGroup group = new SpeedControllerGroup(motor);
        addChild(configCANID, group);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void extend() {
        motor.set(0.5);
    }

    @Override
    public void retract() {
        motor.set(-0.5);
    }

    @Override
    public void stop() {
        motor.set(0);
    }
}