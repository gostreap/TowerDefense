/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.element;

/**
 *
 * @author omar
 */
import towerdefense.model.bullet.Bullet;
import basicobject.Dimension;
import java.util.LinkedList;
import basicobject.Point;
import javafx.scene.paint.Color;
import towerdefense.controller.SoundController;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.attacker.Attacker;
import towerdefense.view.TowerView;

public abstract class Tower extends Element implements java.io.Serializable {

    private static final long serialVersionUID = 201L;

    protected int damage;
    protected int attack_speed;
    protected LinkedList<Bullet> bulletList;
    protected LinkedList<Bullet> trashBullets;
    protected Point canon_direction;
    protected long last_shoot_time = -1;

    private int rotation_speed = 6;
    protected double angle_attacker;
    protected double angle_canoon;
    protected Attacker target_attacker;
    protected Point direction_shoot;
    protected int boost_from_towerboost;
    protected boolean shooted = false;

    //Section pour mesure de performance :
    long timeUdateNearest = 0;
    long timeRotate = 0;
    long timeShoot = 0;
    long timeMoveBullet = 0;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Tower(Dimension dimension, Block block, Board board, int range,
            int attack_speed, int cost, Color color) {
        this(dimension, block, board, range, attack_speed, 0, false, cost, color);
    }

    public Tower(Dimension dimension, Block block, Board board, int range,
            int attack_speed, int damage, int cost, Color color) {
        this(dimension, block, board, range, attack_speed, damage, false,
                cost, color);
    }

    public Tower(Dimension dimension, Block block, Board board, int range,
            int attack_speed, int damage, boolean flying_unit, int cost, Color color) {
        super(dimension, block, board, range, flying_unit, cost, color);
        this.flying_unit = flying_unit;
        this.attack_speed = attack_speed;
        this.damage = damage;
        bulletList = new LinkedList<>();
        trashBullets = new LinkedList<>();
        elementView = new TowerView(block.getPosition(), dimension,
                Block.getDimension(), board.getBoardView(), color,
                board.getPlayer(), this);
    }

    public void shoot(LinkedList<Attacker> attackers) {
        boolean can_shoot = last_shoot_time == -1
                || System.currentTimeMillis() - last_shoot_time > attack_speed * board.getTimeCoef();
        if (can_shoot) {
            last_shoot_time = System.currentTimeMillis();
            if (target_attacker != null) {
                boolean b = (target_attacker.isFlyingUnit() && flying_unit)
                        || (!target_attacker.isFlyingUnit() && !flying_unit);
                if (b) {
                    if (angle_attacker - angle_canoon < 5 && angle_attacker - angle_canoon > -5) {
                        shooted = true;
                        SoundController.SHOOT();
                        addBullets();
                    }
                }
            }
        }
    }

    public void rotate(LinkedList<Attacker> attackers) {
        if (direction_shoot != null) {
            Point centerTower = getCenterPosition();
            Point targetPoint = direction_shoot.add(centerTower);
            Point Tower_Attacker = new Point(targetPoint.getX() - centerTower.getX(), targetPoint.getY() - centerTower.getY());
            angle_attacker = Tower_Attacker.angle(new Point(0, 1));
            angle_canoon = ((TowerView) elementView).getAngle();
            if (direction_shoot.getX() > 0) {
                angle_attacker = -angle_attacker;
            }
            if (Math.abs(angle_canoon - angle_attacker) > 180) {
                angle_attacker = getNegativeAngle(angle_attacker);
            }
            if (Math.abs(angle_attacker - angle_canoon) > rotation_speed) {
                if (angle_attacker < angle_canoon) {
                    ((TowerView) elementView).rotate(-rotation_speed);
                } else {
                    ((TowerView) elementView).rotate(rotation_speed);
                }
            } else if (!Double.isNaN(angle_attacker - angle_canoon) && !(Math.abs(angle_attacker - angle_canoon) < rotation_speed)) {
                ((TowerView) elementView).rotate(angle_attacker - angle_canoon);
            }
        }
    }

    public void update_targetAttacker(LinkedList<Attacker> attackers) {
        if (shooted || target_attacker == null || target_attacker != null && attackers.indexOf(target_attacker) == -1
                || target_attacker != null && !isInRange(target_attacker)) {
//            System.out.println(this.findMode);
            target_attacker = findTargetAttacker(attackers);
            shooted = false;
        }
        if (target_attacker != null) {
            Point posAttacker = target_attacker.getPosition();
            Point centerOfTower = this.getCenterPosition();
            Point directionAttacker = target_attacker.getDirectionBySpeed();
            Point deltaPos = posAttacker.add(centerOfTower.multiply(-1));

            double t = findT(deltaPos, directionAttacker, getSpeed());

            direction_shoot = deltaPos.multiply(1 / (getSpeed() * t)).add(directionAttacker.multiply(1 / getSpeed()));
            Point position_attaque = posAttacker.add(directionAttacker.multiply(t));
            double distance = position_attaque.distance(centerOfTower);
            if (distance >= range * Block.getSize()) {
                shooted = true;
                target_attacker = null;
            }

        }
    }

    @Override
    public void effect(LinkedList<Attacker> attackers) {
        update_targetAttacker(attackers);
        rotate(attackers);
        shoot(attackers);
        moveBullet(attackers);
    }

    protected abstract void addBullets();

    public void addBullet(Bullet b) {
        bulletList.add(b);
    }

    public void removeBullet(Bullet b) {
        trashBullets.add(b);
    }

    public void removeBullets() {
        for (Bullet b : bulletList) {
            b.getBulletView().removeOfBoard();
        }
        bulletList.clear();
        trashBullets.clear();
    }

    public abstract double getSpeed();

    public double findT(Point delta_dir, Point ballDirBySpeed, double speedBall) {
        double X1 = delta_dir.getX();
        double Y1 = delta_dir.getY();
        double X2 = ballDirBySpeed.getX();
        double Y2 = ballDirBySpeed.getY();

        double A = -speedBall * speedBall + X2 * X2 + Y2 * Y2;
        double B = 2 * (X1 * X2 + Y1 * Y2);
        double C = X1 * X1 + Y1 * Y1;

        double delta = B * B - 4 * A * C;
        if (A == 0) {
            return (-C) / B;
        } else if (delta >= 0) {
            double T1 = (-B - Math.sqrt(delta)) / (2 * A);
            double T2 = (-B + Math.sqrt(delta)) / (2 * A);
            if (T2 > 0 && T2 > T1) {
                System.out.println("T2");
                return T2;
            }
            return T1;
        }
        return -1;
    }

    public void moveBullet(LinkedList<Attacker> attackers) {
        for (Bullet b : bulletList) {
            if (b != null) {
                b.move(board);
            }
        }

        for (Bullet b : trashBullets) {
            bulletList.remove(b);
        }
        trashBullets.clear();
    }

    public int getDamage() {
        return damage + boost_from_towerboost;
    }

    public int getBoost_from_towerboost() {
        return boost_from_towerboost;
    }

    public static double getNegativeAngle(double degree) {
        return -(360 - degree);
    }

    @Override
    public int getCoefficient() {
        return (int) (1000 * damage / attack_speed);
    }

    public long[] getTimes() {
        long[] times = {timeUdateNearest, timeRotate, timeShoot, timeMoveBullet};
        return times;
    }

    @Override
    public void resize(double factor) {
        elementView.resize(block);

        for (Bullet bullet : bulletList) {
            bullet.resize(factor);
        }
    }

    public void resetElementView() {
        elementView = new TowerView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), colorN.getColor(), board.getPlayer(), this);
        for (Bullet bullet : bulletList) {
            bullet.resetBulletView(board);
        }
    }

}
