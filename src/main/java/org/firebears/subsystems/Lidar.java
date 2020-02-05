package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.firebears.sensors.LidarLite;

import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lidar extends SubsystemBase {



    private double distance = 0;
    private int status = 0;
    public double getDistance(){
        return distance;
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
