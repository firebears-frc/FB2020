package org.firebears.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

/// Interface for climber subsystem (SRX or SparkMAX)
public interface Climber extends Subsystem {

    void extend();

    void retract();

    void stop();
}