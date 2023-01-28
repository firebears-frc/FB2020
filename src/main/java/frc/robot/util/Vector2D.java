package frc.robot.util;

public class Vector2D {
    private int x;
    private int y;

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int nx) { x = nx; }
    public void setY(int ny) { x = ny; }

    public Vector2D(int nx,int ny){
      x = nx;
      y = ny;
    }

    public void setVector(int nx,int ny){
      x = nx;
      y = ny;
    }
  }