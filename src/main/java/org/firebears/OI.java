package org.firebears;

import org.firebears.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {
    public XboxController xboxController;
    private JoystickButton lowerAcqButton; //LB
    private JoystickButton raiseAcqButton; //RB
    private JoystickButton spitButton; //B
    

    public Joystick buttonBox;
    private JoystickButton shootButton; //1
    private JoystickButton indexButton; //2
    private JoystickButton rightClimbUpSwitch;
    private JoystickButton rightClimbDownSwitch;
    private JoystickButton leftClimbUpSwitch;
    private JoystickButton leftClimbDownSwitch;
    private JoystickButton celebrateButton;

    public OI() {
        xboxController = new XboxController(0);

        lowerAcqButton = new JoystickButton(xboxController, 5); //LB
        lowerAcqButton.whenPressed(new AcquisitionLowerCommand(
            Robot.acquisition, Robot.loader));

        raiseAcqButton = new JoystickButton(xboxController, 6); //RB
        raiseAcqButton.whenPressed(new AcquisitionRaiseCommand(
            Robot.acquisition, Robot.loader));

        spitButton = new JoystickButton(xboxController, 2); //B
        spitButton.whenPressed(new SpitCommand(Robot.acquisition, Robot.loader));

        buttonBox = new Joystick(1);
        
        shootButton = new JoystickButton(buttonBox, 1);
        shootButton.whenHeld(new yeEt(Robot.storage, Robot.shooter));

        indexButton = new JoystickButton(buttonBox, 11);
        indexButton.whenPressed(new BallQueueCommand(Robot.storage));

        rightClimbUpSwitch = new JoystickButton(buttonBox, 6);
        rightClimbUpSwitch.whenHeld(new ClimberExtendCommand(Robot.climberRight));

        rightClimbDownSwitch = new JoystickButton(buttonBox, 4);
        rightClimbDownSwitch.whenHeld(new ClimberRetractCommand(Robot.climberRight));

        leftClimbUpSwitch = new JoystickButton(buttonBox, 5);
        leftClimbUpSwitch.whenHeld(new ClimberExtendCommand(Robot.climberLeft));

        leftClimbDownSwitch = new JoystickButton(buttonBox, 3);
        leftClimbDownSwitch.whenHeld(new ClimberRetractCommand(Robot.climberLeft));

        celebrateButton = new JoystickButton(buttonBox, 12);
        celebrateButton.whenHeld(new CelebrateCommand());
    }

    public XboxController getXboxController() {
        return xboxController;
    }

    public Joystick getJoystick1() {
        return buttonBox;
    }
}