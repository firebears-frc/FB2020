

package org.firebears.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.firebears.commands.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj2.command.Subsystem;






public class Climber extends SubsystemBase {

    static final Preferences config = Preferences.getInstance();

    private TalonSRX rightClimber;
    private TalonSRX leftClimber;

    

    

    

    

    public Climber() {

        
        int rightClimberCanID = config.getInt("climber.rightClimber.canID", 8);
        rightClimber = new TalonSRX(rightClimberCanID);
        rightClimber.setInverted(false);

        int leftClimberCanID = config.getInt("climber.leftClimber.canID", 9);
        leftClimber = new TalonSRX(leftClimberCanID);
        leftClimber.setInverted(false);
        
    }

    @Override
    public void periodic() {
        

    }

    

    

    
    
    public void rightClimberUp() {
        rightClimber.set(ControlMode.PercentOutput, 1);
    }

    public void rightClimberDown() {
        rightClimber.set(ControlMode.PercentOutput, -1);
    }

    public void rightClimberStop() {
        rightClimber.set(ControlMode.PercentOutput, 0);
    }

    public void leftClimberUp() {
        leftClimber.set(ControlMode.PercentOutput, 1);
    }

    public void leftClimberDown() {
        leftClimber.set(ControlMode.PercentOutput, -1);
    }

    public void leftClimberStop() {
        leftClimber.set(ControlMode.PercentOutput, 0);
    }

}
