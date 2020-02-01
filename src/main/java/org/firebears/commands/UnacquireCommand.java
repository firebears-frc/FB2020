

package org.firebears.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.firebears.Robot;


public class UnacquireCommand extends CommandBase {

    

    

    
    public UnacquireCommand() {
        addRequirements(Robot.acquisition);

        
        

        
        

        
    }

    
    @Override
    public void initialize() {
    }

    
    @Override
    public void execute() {
        Robot.acquisition.endAcquire();
    }

    
    @Override
    public boolean isFinished() {
        return false;
    }

    
    @Override
    public void end(boolean interrupted) {
    }

}
