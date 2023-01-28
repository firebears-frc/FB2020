package frc.robot.util;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionTarget {
    private int FID = -1;
    public Transform3d TargetTrans;
    public Vector2D TargetGlobalPos;
    public float LastSeen;
  
    public VisionTarget(int ID, int Xcm, int Ycm) {
      FID = ID;
      TargetGlobalPos = new Vector2D(Xcm, Ycm);
    }
    private Double Round(Double x){
      return Math.round(x * 100.0) / 100.0;
    }

    public void HasSeenTarget(Transform3d TargetTransform,float LastTimeSaw){
      TargetTrans = TargetTransform;
      LastSeen = LastTimeSaw;

      if(TargetTrans != null) {
        SmartDashboard.putString(String.format("%d Target", FID), Round((Double)TargetTrans.getX()) + "," + Round((Double)TargetTrans.getY()) + ',' + Round((Double)TargetTrans.getZ()));
      }
      else{
        SmartDashboard.putString(String.format("%d Target", FID), "Target Not Here Cuh");
      }
    }
  }