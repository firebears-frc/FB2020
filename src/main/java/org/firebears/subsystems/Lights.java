package org.firebears.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.firebears.Robot;

public class Lights extends SubsystemBase {
    public static final int MAX_ANIMATIONS = 10;
    public static final int MAX_PIXELSTRIPS = 3;
    public static final int BRIGHTNESS = 125;
    public static final int I2C_ADDRESS = 4;

    public static final int BLUE_ANIMATION = 0;
    public static final int RED_ANIMATION = 1;
    public static final int CELEBRATE_ANIMATION = 2;
    public static final int SHOOTING_ANIMATION = 3;
    public static final int DEFAULT_ANIMATION = 4;
    public static final int ONEBALL_ANIMATION = 5;
    public static final int TWOBALL_ANIMATION = 6;
    public static final int THREEBALL_ANIMATION = 7;
    public static final int FOURBALL_ANIMATION = 8;
    public static final int FIVEBALL_ANIMATION = 9;

    public static final int PIXEL_STRIP = 0;

    public final I2C i2c;
    private final DriverStation driverstation;
    private final boolean DEBUG;
    private byte[] dataOut = new byte[2];
    private byte[] dataBack = new byte[0];
    private int[] currentAnimation = new int[MAX_PIXELSTRIPS];
    private int[] nextAnimation = new int[MAX_PIXELSTRIPS];

    public Lights() {
        final Preferences config = Preferences.getInstance();
        i2c = new I2C(Port.kOnboard, config.getInt("lights.i2cAddress", 4));
        driverstation = DriverStation.getInstance();
        DEBUG = config.getBoolean("debug", false);
    }

    private synchronized void setAnimation(int s, int a) {
        nextAnimation[s] = a;
    }

    private synchronized boolean sendAllAnimations() {
        boolean messagesSent = false;
        for (int s = 0; s < MAX_PIXELSTRIPS; s++) {
            if (nextAnimation[s] != currentAnimation[s]) {
                int a = nextAnimation[s];
                dataOut[0] = (byte) (s + '0');
                dataOut[1] = (byte) (a + '0');
                i2c.transaction(dataOut, dataOut.length, dataBack, dataBack.length);
                currentAnimation[s] = a;
                if (DEBUG) {
                    System.out.printf("Lights: setAnimation(%d, %d)%n", s, a);
                }
                messagesSent = true;
            }
        }
        return messagesSent;
    }

    boolean isCelebrating = false;
    public void celebrate(boolean isCelebrating) {
        this.isCelebrating = isCelebrating;
    }

    boolean isShooting = false;
    public void shoot(boolean isShooting) {
        this.isShooting = isShooting;
    }

    // This method will be called once per scheduler run
    @Override
    public void periodic() {
        Robot.storage.getPowerCellCount();
        if (driverstation.getAlliance().equals(Alliance.Blue)) {
            setAnimation(PIXEL_STRIP, BLUE_ANIMATION);
        } else if(driverstation.getAlliance().equals(Alliance.Red)) {
            setAnimation(PIXEL_STRIP, RED_ANIMATION);
        } else if (driverstation.isDisabled()) {
            setAnimation(PIXEL_STRIP, DEFAULT_ANIMATION);
        } else if(isCelebrating) {
            setAnimation(PIXEL_STRIP, CELEBRATE_ANIMATION);
        } else if(Robot.storage.getPowerCellCount() == 1) {
            setAnimation(PIXEL_STRIP, ONEBALL_ANIMATION);
        } else if(Robot.storage.getPowerCellCount() == 2) {
            setAnimation(PIXEL_STRIP, TWOBALL_ANIMATION);
        } else if(Robot.storage.getPowerCellCount() == 3) {
            setAnimation(PIXEL_STRIP, THREEBALL_ANIMATION);
        } else if(Robot.storage.getPowerCellCount() == 4) {
            setAnimation(PIXEL_STRIP, FOURBALL_ANIMATION);
        } else if(Robot.storage.getPowerCellCount() == 5) {
            setAnimation(PIXEL_STRIP, FIVEBALL_ANIMATION);
        } else if (isShooting) {
            setAnimation(PIXEL_STRIP, SHOOTING_ANIMATION);
        } else {
            setAnimation(PIXEL_STRIP, DEFAULT_ANIMATION);
        }
    }
}
