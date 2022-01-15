package org.firebears.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.firebears.sensors.LidarLite;

public class Lidar extends SubsystemBase {

    /** Thread for I2C communication */
    private final Thread i2c_thread = new Thread() {
        @Override
        public void run() {
            LidarLite lidar = new LidarLite();
            MedianFilter mfilter = new MedianFilter(5);
            lidar.startContinuous();
            while (true) {
                for (int i = 0; i < 20; i++) {
                    int d = lidar.getDistance();
                    if (d >= 0) {
                        distance = mfilter.calculate(d);
                    } else {
                        distance = -1;
                        mfilter.reset();
                        break;
                    }
                    Timer.delay(0.05);
                }
                status = lidar.getStatus();
            }
        }
    };

    /** Distance in cm */
    private double distance = -1;

    /** Get distance in m */
    public double getDistance() {
        if (LidarLite.isError(status))
            return -1;
        else
            return distance * 100.0;
    }

    /** Status flags */
    private int status = 0;

    /** Get status flags */
    public int getStatus() {
        return status;
    }

    private final Preferences config = Preferences.getInstance();
    private final long dashDelay;
    private long dashTimeout;

    private final ShuffleboardTab tab = Shuffleboard.getTab("Lidar");
    private final NetworkTableEntry errorWidget;
    private final NetworkTableEntry distanceWidget;

    public Lidar() {
        dashDelay = config.getLong("dashDelay", 250);
        dashTimeout = System.currentTimeMillis() + dashDelay + 100;
        errorWidget = tab.add("errors", "").withPosition(0, 0).getEntry();
        distanceWidget = tab.add("distance", 0).withPosition(1, 0)
            .withSize(3, 3).getEntry();
        i2c_thread.start();
    }

    @Override
    public void periodic() {
        long now = System.currentTimeMillis();
        if (now > dashTimeout) {
            errorWidget.setString(LidarLite.getErrors(status));
            distanceWidget.setNumber(distance);
            dashTimeout = now + dashDelay;
        }
    }
 }