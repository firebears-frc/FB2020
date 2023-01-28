package frc.robot.util;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionMap {
    private VisionTarget[] Targets = new VisionTarget[8];
    private int LastSeen;
    private Vector2D OurPosition;
    public Field2d F2d;

    public VisionMap(){
      Targets[0] = new VisionTarget(0, -689, 109);
      Targets[3] = new VisionTarget(3, -689, 280);
      Targets[5] = new VisionTarget(5, -689, 451);
      OurPosition = new Vector2D(0, 0);
      F2d = new Field2d();
    }
    private Double Round(Double x){
        return Math.round(x * 100.0) / 100.0;
      }

    public void onVisionTargetSeen(PhotonTrackedTarget Target){
      int FID = Target.getFiducialId();
      if(FID < Targets.length){
        Targets[FID].HasSeenTarget(Target.getBestCameraToTarget(),System.currentTimeMillis());
        LastSeen = FID;
      }

      //Get / Update Our Position Based On Targets Seen
      Transform3d pos = Targets[LastSeen].TargetTrans;
      double x;
      double y;

      if(Targets[LastSeen].TargetGlobalPos.getX() < 0){
        x = Targets[LastSeen].TargetGlobalPos.getX() + (pos.getX()*100);
        y = Targets[LastSeen].TargetGlobalPos.getY() + (pos.getY()*100);
      }
      else{
        x = Targets[LastSeen].TargetGlobalPos.getX() - (pos.getX()*100);
        y = Targets[LastSeen].TargetGlobalPos.getY() - (pos.getY()*100);
      }

      OurPosition.setVector((int)x, (int)y);
      SmartDashboard.putString("Our Robot Position", "(" + Round(x) + "cm ," + Round(y) + "cm )");
      F2d.setRobotPose(new Pose2d((x/100) + 8,y/100,new Rotation2d(180)));
      SmartDashboard.putData(F2d);

    }
  }