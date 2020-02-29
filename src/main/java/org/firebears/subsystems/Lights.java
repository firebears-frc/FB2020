package org.firebears.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lights extends SubsystemBase {

    /** Thread for I2C communication */
    private final Thread i2c_thread = new Thread() {
        @Override
        public void run() {
            while (true) {
                sendAllAnimations();
                Timer.delay(0.1);
            }
        }
    };

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

    private static final int PIXEL_STRIP = 0;

    private final I2C i2c;
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
        i2c_thread.start();
    }

    private void setAnimation(int s, int a) {
        nextAnimation[s] = a;
    }

    private boolean sendAllAnimations() {
        boolean messagesSent = false;
        for (int strip = 0; strip < MAX_PIXELSTRIPS; strip++) {
            int n = nextAnimation[strip];
            int c = currentAnimation[strip];
            if (n != c) {
                sendAnimation(strip, n);
                messagesSent = true;
                currentAnimation[strip] = n;
            }
        }
        return messagesSent;
    }

    private void sendAnimation(int strip, int a) {
        dataOut[0] = (byte) (a + '0');
        dataOut[1] = (byte) (a + '0');
        i2c.transaction(dataOut, dataOut.length, dataBack, dataBack.length);
        if (DEBUG) {
            System.out.printf("Lights: setAnimation(%d, %d)%n", strip, a);
        }
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
        setAnimation(PIXEL_STRIP, getAnimation());
    }

    private int getAnimation() {
        if (driverstation.isDisabled())
            return DEFAULT_ANIMATION;
        else if (isCelebrating)
            return CELEBRATE_ANIMATION;
        else if (isShooting)
            return SHOOTING_ANIMATION;
        //else if (count > 0)
        //    return getCountAnimation(count);
        else if (driverstation.getAlliance().equals(Alliance.Blue))
            return BLUE_ANIMATION;
        else
            return RED_ANIMATION;
    }

    private int getCountAnimation(int count) {
        switch (count) {
            case 1:
                return ONEBALL_ANIMATION;
            case 2:
                return TWOBALL_ANIMATION;
            case 3:
                return THREEBALL_ANIMATION;
            case 4:
                return FOURBALL_ANIMATION;
            case 5:
                return FIVEBALL_ANIMATION;
            default:
                return DEFAULT_ANIMATION;
        }
    }
}