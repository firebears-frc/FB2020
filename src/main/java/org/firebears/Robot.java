package org.firebears;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.commands.*;
import org.firebears.commands.autoCommands.AutoRoutines.*;
import org.firebears.subsystems.*;

public class Robot extends TimedRobot {

    private final Preferences config = Preferences.getInstance();

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    public static Chassis chassis;
    public static Acquisition acquisition;
    public static Loader loader;
    public static Shooter shooter;
    public static Climber climberRight;
    public static Climber climberLeft;
    public static Vision vision;
    public static Lidar lidar;
    public static Storage storage;
    public static Lights lights;

    private final ShuffleboardTab driversTab = Shuffleboard.getTab("Drivers");
    private NetworkTableEntry isFilled;
    private NetworkTableEntry lidarDistance;
    private NetworkTableEntry visionConfidence;
    private NetworkTableEntry isShooterSpunUp;
    private NetworkTableEntry isInPosition;
    private NetworkTableEntry cameraScreen;

    @Override
    public void robotInit() {

        chassis = new Chassis();
        loader = new Loader();
        acquisition = new Acquisition();
        shooter = new Shooter();
        climberLeft = createClimber("climber.left.canID", 22);
        climberRight = createClimber("climber.right.canID", 24);
        vision = new Vision();
        lidar = new Lidar();
        storage = new Storage();
        lights = new Lights();

        CommandScheduler.getInstance().registerSubsystem(vision);
        CommandScheduler.getInstance().registerSubsystem(lidar);

        UsbCamera camera = CameraServer.startAutomaticCapture(0);

        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser
        chooser.setDefaultOption("Shoot and Drive", new Auto2(chassis, storage, shooter));
        chooser.addOption("Drive Back", new Auto7(chassis));
        chooser.addOption("Pick Up Balls", new Auto6());

        SmartDashboard.putData("Auto mode", chooser);

        isFilled = driversTab.add("Is Filled", 0).withPosition(0, 0).getEntry();
        lidarDistance = driversTab.add("Lidar Distance", 0).withPosition(0, 1).getEntry();
        visionConfidence = driversTab.add("Vision Confidence", 0).withPosition(1, 0).getEntry();
        isShooterSpunUp = driversTab.add("Is Shooter Spun Up", 0).withPosition(3, 2).getEntry();
        isInPosition = driversTab.add("Is In Position", 0).withPosition(2, 0).getEntry();
        driversTab.add("Reset Button", new ResetCommand(storage)).withPosition(1, 1);
        cameraScreen = driversTab.add("Camera Screen", 0).withPosition(4, 0).getEntry();
        driversTab.add("Auto Selection", chooser).withPosition(0, 4);

    }

    private Climber createClimber(String configCANID, int defCANID) {
        if (config.getString("climber.type", "sparkMax").equals("srx")) {
            return new ClimberSRX(configCANID, defCANID);
        } else {
            return new ClimberSparkMAX(configCANID, defCANID);
        }
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        isFilled.setBoolean(storage.isBackedUp() && storage.isIndexFull());
        lidarDistance.setNumber(lidar.getDistance());
        visionConfidence.setNumber(vision.getTargetConfidence());
        isShooterSpunUp.setBoolean(shooter.isWheelSpunUp());
        isInPosition.setBoolean(storage.getPositionSensor());
    }

    /**
     * This function is called when the disabled button is hit. You can use it to
     * reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void autonomousInit() {

        chassis.setBrake(true);
        chassis.resetEncoders();
        
        setDefaultCommands();

        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.schedule();
        

        //new ResetCommand(storage).schedule(); 
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.


        if (autonomousCommand != null)
            autonomousCommand.cancel();     
        setDefaultCommands();   

        chassis.setBrake(false);

        new ResetCommand(storage).schedule(); 
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void testInit() {
        acquisition.setDefaultCommand(null);
        chassis.setDefaultCommand(null);
        storage.setDefaultCommand(null);
    }
    
    public void setDefaultCommands(){
        acquisition.setDefaultCommand(new AcquisitionRaiseCommand(acquisition, loader, false));
        chassis.setDefaultCommand(new DriveCommand(chassis));
        storage.setDefaultCommand(new TheOneCommandtoRuleThemAll(storage));
        shooter.setDefaultCommand(new idleTHATwheel(shooter));
    }
}