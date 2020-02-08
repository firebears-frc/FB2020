package org.firebears.subsystems;

import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.firebears.sensors.LidarLite;

public class Lidar extends SubsystemBase {

    private double distance = 0;

    /** Get distance in cm */
    public double getDistance() {
        return distance;
    }

    private int status = 0;

    /** Get status flags */
    public int getStatus() {
        return status;
    }

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
                        mfilter.reset();
                        break;
                    }
                    Timer.delay(0.05);
                }
                status = lidar.getStatus();
            }
        }
    };

    public Lidar() {
        super();
        i2c_thread.start();
    }
 }