
package org.firebears.subsystems;

//import org.firebears.commands.*;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;






public class Acquisition extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();

    private CANSparkMax lower;
    private CANSparkMax spin;
    
    
    

    public Acquisition() {

        

        int acquisitionLowerCanID = config.getInt("acquisition.lower.canID", 2);
        lower = new CANSparkMax(acquisitionLowerCanID, MotorType.kBrushless);
        lower.setInverted(false);

        int acquisitionSpinCanID = config.getInt("acquisition.spin.canID", 6);
        spin = new CANSparkMax(acquisitionSpinCanID, MotorType.kBrushless);
        spin.setInverted(false);
        
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("lowerSpeed", lower.get());
        SmartDashboard.putNumber("spinSpeed", spin.get());

        

    }

    public void startAcquire() {
        lower.set(1);
        spin.set(1);
    }

    public void endAcquire() {
        lower.set(-1);
        spin.set(-1);
    }
    

    

    
    

}
