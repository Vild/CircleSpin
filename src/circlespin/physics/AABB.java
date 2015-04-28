package circlespin.physics;

import circlespin.data.Vec2;
import circlespin.data.Vec4;

import java.util.ArrayList;
import java.util.List;

public class AABB {
  private double x, y;
  private double w, h;
  private double vx, vy;
  private double speed;
  private boolean xBlock, yBlock;
  private boolean hitLeft, hitRight;
  private boolean hitRoof, hitGround;

  private double maxSpeed = 10000;

  public AABB(double x, double y, double w, double h, double vx, double vy,
              double speed) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.vx = vx;
    this.vy = vy;
    this.speed = speed;

    xBlock = yBlock = false;
    hitLeft = hitRight = false;
    hitRoof = hitGround = false;
  }

  public void Update(double delta, double dvx, double dvy,
                     List<AABB> hitboxes, Vec2 jumpyness) {
    ArrayList<Vec4> newHitbox = new ArrayList<Vec4>();
    for (AABB aabb : hitboxes)
      newHitbox.add(aabb.Rect());

    Update(delta, dvx, dvy, newHitbox, jumpyness);
  }

  public void Update(double delta, double dvx, double dvy,
                     ArrayList<Vec4> hitboxes, Vec2 jumpyness) {
    vx += dvx;
    vy += dvy;

    vx = Math.max(Math.min(vx, maxSpeed), -maxSpeed);
    vy = Math.max(Math.min(vy, maxSpeed), -maxSpeed);

    x += vx * delta;

    xBlock = hitLeft = hitRight = false;
    for (Vec4 box : hitboxes) {
      if (Hit(box)) {
        xBlock = true;
        if (vx < 0)
          hitLeft = true;
        else
          hitRight = true;

        x -= vx * delta;
        if (jumpyness.x == 0)
          vx = 0;
        else
          vx = -vx / jumpyness.x;
        break;
      }
    }

    y += vy * delta;
    yBlock = hitRoof = hitGround = false;
    for (Vec4 box : hitboxes) {
      if (Hit(box)) {
        yBlock = true;
        if (vy < 0)
          hitRoof = true;
        else
          hitGround = true;
        y -= vy * delta;

        if (jumpyness.y == 0)
          vy = 0;
        else
          vy = -vy / jumpyness.y;
        break;
      }
    }
  }

  public boolean Hit(AABB b) {
    return CheckCollision(Rect(), b.Rect());
  }

  public boolean Hit(Vec4 b) {
    return CheckCollision(Rect(), b);
  }

  private double LengthTo(Vec2 b) {
    return LengthTo(Point(), b);
  }

  private boolean CheckCollision(Vec4 rect1, Vec4 rect2) {
    double left1 = rect1.x;
    double right1 = rect1.x + rect1.w;
    double top1 = rect1.y;
    double bottom1 = rect1.y + rect1.h;

    double left2 = rect2.x;
    double right2 = rect2.x + rect2.w;
    double top2 = rect2.y;
    double bottom2 = rect2.y + rect2.h;

    return !((left1 > right2) || (right1 < left2) || (top1 > bottom2) || (bottom1 < top2));
  }

  private double LengthTo(Vec2 p1, Vec2 p2) {
    double xl = Math.min(p1.x, p2.x);
    double xg = Math.max(p1.x, p2.x);
    double xd = xg - xl;

    double yl = Math.min(p1.y, p2.y);
    double yg = Math.max(p1.y, p2.y);
    double yd = yg - yl;

    return Math.sqrt(xd * xd + yd * yd);
  }

  public Vec4 Rect() {
    return new Vec4(x, y, w, h);
  }

  public Vec2 Point() {
    return new Vec2(x, y);
  }

  public boolean XBlocking() {
    return xBlock;
  }

  public boolean YBlocking() {
    return yBlock;
  }

  public boolean HitLeft() {
    return hitLeft;
  }

  public boolean HitRight() {
    return hitRight;
  }

  public boolean HitRoof() {
    return hitRoof;
  }

  public boolean HitGround() {
    return hitGround;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getW() {
    return w;
  }

  public void setW(double w) {
    this.w = w;
  }

  public double getH() {
    return h;
  }

  public void setH(double h) {
    this.h = h;
  }

  public double getVx() {
    return vx;
  }

  public void setVx(double vx) {
    this.vx = vx;
  }

  public double getVy() {
    return vy;
  }

  public void setVy(double vy) {
    this.vy = vy;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

}
