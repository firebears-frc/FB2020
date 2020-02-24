package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.firebears.commands.ResetCommand;

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

    private final DigitalInput positionSensor;
    private final DigitalInput eye1;
    private final DigitalInput eye2;
    private final DigitalInput eye3;
    private final DigitalInput eye4;
    private final DigitalInput eye5;
    private final CANEncoder indexEncoder;
    private final SpeedControllerGroup group;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Storage");

    private final NetworkTableEntry magnetWidget;
    private final NetworkTableEntry countWidget;
    private final NetworkTableEntry eye1Widget;
    private final NetworkTableEntry eye2Widget;
    private final NetworkTableEntry eye3Widget;
    private final NetworkTableEntry eye4Widget;
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
        indexMotor.setInverted(false);
        err = indexMotor.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        indexEncoder = indexMotor.getEncoder();
        group = new SpeedControllerGroup(indexMotor);
        addChild("SpinMotor", group);
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on indexMotor");


        positionSensor = new DigitalInput(config.getInt("storage.position.dio", 0));
        eye1 = new DigitalInput(config.getInt("storage.eye1.dio", 1));
        eye2 = new DigitalInput(config.getInt("storage.eye2.dio", 2));
        eye3 = new DigitalInput(config.getInt("storage.eye3.dio", 3));
        eye4 = new DigitalInput(config.getInt("storage.eye4.dio", 4));
        eye5 = new DigitalInput(config.getInt("storage.eye5.dio", 5));

        //magnetWidget = tab.add("magnet", 0).withPosition(0, 0).getEntry();
        magnetWidget = tab.add("positionSensorValue", false).getEntry();
        countWidget = tab.add("count", 0).withPosition(0, 1).getEntry();
        eye1Widget = tab.add("position1Filled", false).getEntry();
        eye2Widget = tab.add("position2Filled", false).getEntry();
        eye3Widget = tab.add("position3Filled", false).getEntry();
        eye4Widget = tab.add("position4Filled", false).getEntry();
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
            countWidget.setNumber(getPowerCellCount());
            dashTimeout = now + dashDelay;
            eye1Widget.setBoolean(eye1.get());
            eye2Widget.setBoolean(eye2.get());
            eye3Widget.setBoolean(eye3.get());
            eye4Widget.setBoolean(eye4.get());
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

    public int getPowerCellCount() {
        int count = 0;
        if (eye1.get()) 
            count ++; 
        if (eye2.get()) 
            count ++; 
        if (eye3.get()) 
            count ++; 
        if (eye4.get()) 
            count ++; 
        if (eye5.get()) 
            count ++;
        return count;
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
}
