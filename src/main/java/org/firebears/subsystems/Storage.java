package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.firebears.commands.ResetCommand;
import org.firebears.util.PIDSparkMotor;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Storage extends SubsystemBase {
    private final Preferences config = Preferences.getInstance();

    private final CANSparkMax indexMotor;
    private final PIDSparkMotor PIDindexMotor;

    private final DigitalInput positionSensor;
    private final DigitalInput eye1;
    private final DigitalInput eye5;
    private final CANEncoder indexEncoder;
    private final SpeedControllerGroup group;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Storage");

    private final NetworkTableEntry magnetWidget;
    private final NetworkTableEntry eye1Widget;
    private final NetworkTableEntry eye5Widget;

    private final long dashDelay;
    private long dashTimeout = 0;

    public Storage() {
        CANError err;
        int stallLimit = config.getInt("storage.stallLimit", 20);
        int freeLimit = config.getInt("storage.freeLimit", 10);
        int limitRPM = config.getInt("storage.limitRPM", 500);
        int indexMotorCanID = config.getInt("storage.indexMotor.canID", 10);
        
        indexMotor = new CANSparkMax(indexMotorCanID, MotorType.kBrushless);

        PIDindexMotor = new PIDSparkMotor(indexMotor,
        config.getDouble("storage.P", 0.0), 
        config.getDouble("storage.I", 0.0), 
        config.getDouble("storage.D", 0.0));

        indexMotor.setInverted(false);
        err = indexMotor.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        indexEncoder = indexMotor.getEncoder();
        group = new SpeedControllerGroup(indexMotor);
        addChild("SpinMotor", group);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on indexMotor");


        positionSensor = new DigitalInput(config.getInt("storage.position.dio", 0));
        eye1 = new DigitalInput(config.getInt("storage.eye1.dio", 1));
        eye5 = new DigitalInput(config.getInt("storage.eye5.dio", 5));

        //magnetWidget = tab.add("magnet", 0).withPosition(0, 0).getEntry();
        magnetWidget = tab.add("positionSensorValue", false).getEntry();
        eye1Widget = tab.add("position1Filled", false).getEntry();
        eye5Widget = tab.add("position5Filled", false).getEntry();
        tab.add("reset", new ResetCommand(this));

        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 125;
    }

    @Override
    public void periodic() {
        boolean magnet = getPositionSensor();
        if (magnet) {
            indexEncoder.setPosition(0.0);
        }
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            magnetWidget.setBoolean(magnet);
            dashTimeout = now + dashDelay;
            eye1Widget.setBoolean(eye1.get());
            eye5Widget.setBoolean(eye5.get());
        }
    }

    public void move() {
        indexMotor.set(0.1);
        
    }

    public void stop() {
        indexMotor.set(0.0);
    }

    public boolean getPositionSensor() {
        return !positionSensor.get();
    }

    public double resetEncoder() {
        indexEncoder.setPosition(0.0);
        return indexEncoder.getPosition();
    }

    public boolean needsIndexing() {
        return (eye1.get() && !eye5.get());
    }

    public boolean isBackedUp() {
        return eye1.get();
    }

    public boolean isIndexFull(){
        return eye5.get();
    }
}
