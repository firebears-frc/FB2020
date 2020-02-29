package org.firebears;

import org.firebears.commands.AcquisitionLowerCommand;
import org.firebears.commands.AcquisitionRaiseCommand;
import org.firebears.commands.BallQueueCommand;
import org.firebears.commands.CelebrateCommand;
import org.firebears.commands.ClimberExtendCommand;
import org.firebears.commands.ClimberRetractCommand;
import org.firebears.commands.SpitCommand;
import org.firebears.commands.autoCommands.ShootAllCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;;

public class OI {
    public final XboxController xboxController;
    private final JoystickButton lowerAcqButton; //LB
    private final JoystickButton raiseAcqButton; //RB
    private final JoystickButton spitButton; //B

    private final Joystick buttonBox;
    private final JoystickButton shootButton; //1
    private final JoystickButton indexButton; //2
    private final JoystickButton rightClimbUpSwitch;
    private final JoystickButton rightClimbDownSwitch;
    private final JoystickButton leftClimbUpSwitch;
    private final JoystickButton leftClimbDownSwitch;
    private final JoystickButton celebrateButton;

    public OI() {
        xboxController = new XboxController(0);

        lowerAcqButton = new JoystickButton(xboxController, 5); //LB
        lowerAcqButton.whenPressed(new AcquisitionLowerCommand(
            Robot.acquisition, Robot.loader, Robot.storage));

        raiseAcqButton = new JoystickButton(xboxController, 6); //RB
        raiseAcqButton.whenPressed(new AcquisitionRaiseCommand(
            Robot.acquisition, Robot.loader, true));

        spitButton = new JoystickButton(xboxController, 2); //B
        spitButton.whenPressed(new SpitCommand(Robot.acquisition, Robot.loader));

        buttonBox = new Joystick(1);

        shootButton = new JoystickButton(buttonBox, 1);
        shootButton.whenPressed(new ShootAllCommand());

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
