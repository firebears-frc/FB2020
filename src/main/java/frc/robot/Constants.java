// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final int CONFIG_SET_TIMEOUT_MS = 0; // 0 for no blocking / checking (30 in main branch)

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static class StorageConstants {
    public static final int INDEX_MOTOR_ID = 12;
    public static final int ALIGNED_SENSOR_CHANNEL = 0;
  }
  public static class ShooterConstants {
    public static final int SHOOTER_MOTOR_ID = 25;
    public static final double SHOOTER_SHOOT_SPEED = 1.0;
    public static final double SHOOTER_IDLE_SPEED = 0.25;
    public static final int PEAK_CURRENT_LIMIT = 25;
    public static final int PEAK_CURRENT_DURATION_MS = 3000;
    public static final int CONTINUOUS_CURRENT_LIMIT = 10;
  }
  public static class LoaderConstants {
    public static final int LEFT_BELT_MOTOR_ID = 20;
    public static final int RIGHT_BELT_MOTOR_ID = 21;
    public static final int PEAK_CURRENT_LIMIT = 15;
    public static final int CONTINUOUS_CURRENT_LIMIT = 10;
    public static final int PEAK_CURRENT_DURATION_MS = 5000;
  }
  public static class AcquisitionConstants {
    public static final int STARS_MOTOR_ID = 10;
    public static final int STARS_PEAK_CURRENT_LIMIT = 15;
    public static final int STARS_PEAK_CURRENT_DURATION_MS = 5000;
    public static final int STARS_CONTINUOUS_CURRENT_LIMIT = 10;
  }
}
