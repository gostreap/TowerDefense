/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.attacker;

/**
 *
 * @author aissatou
 */
import basicobject.ColorN;
import basicobject.Point;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.view.AttackerView;

public abstract class Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 100L;

    private Block currentBlock;
    protected Point position;
    private final double maxSpeed;
    private double speed;
    private int healthPoint;
    private final int maxHealthPoint;
    private int venomDamage;
    private long lastVenomDamageTime;
    private long firstVenomDamageTime;
    private int damage;
    private LinkedList<Point> road;
    protected final Board board;
    private final int size;
    private transient AttackerView attackerView;
    private ColorN colorN;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Attacker(Point position, double speed, int healthPoint, Board b, Color color, int wave, int damage, int size) {
        this.position = position;
        this.speed = speed * Block.getSize() / 20;
        this.maxSpeed = this.speed;
        this.healthPoint = (int) (healthPoint + (wave * Math.sqrt(wave) / 3 * (healthPoint / 10)));
        this.maxHealthPoint = this.healthPoint;
        this.board = b;
        this.damage = damage;
        this.size = size;
        this.colorN = new ColorN(color);
        attackerView = new AttackerView(position, size, board.getBoardView(), color);
    }

    public boolean isFlyingUnit() {
        return false;
    }

    public void reduceSpeed(double speed, double percent) {
        if (speed > 0) {
            this.speed -= speed;
            if ((double) this.speed / maxSpeed <= percent) {
                this.speed = percent * maxSpeed;
            }

        }
    }

    public void increaseSpeed(double speed) {
        if (speed > 0) {
            if (this.speed + speed < maxSpeed) {
                this.speed += speed;
            } else {
                this.speed = maxSpeed;
            }
        }
    }

    public void kill(Player p) {
        if (healthPoint <= 0) {
            int point = getCoefficient();
            p.addMoney(point);
            p.increaseScore(point);
            remove();
        }
    }

    public void effect(LinkedList<Attacker> attackers) {
        attackerView.updateColor();
        takeVenomDamage();
        move();
    }

    public void takeDamage(int damage) {
        Player p = board.getPlayer();
        this.healthPoint -= damage;
        attackerView.takeDamage((double) healthPoint / maxHealthPoint, damage);
        if (healthPoint <= 0) {
            kill(p);
        }
    }

    private void takeVenomDamage() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - firstVenomDamageTime <= 3000 * board.getTimeCoef()
                && currentTime - lastVenomDamageTime >= 100 * board.getTimeCoef()) {
            takeDamage(venomDamage);
            lastVenomDamageTime = currentTime;
        }
    }

    public LinkedList<Point> findBestPath() {
        road = board.findPath(this);
        return road;
    }

    public void setPosition(Point point) {
        position = point;
        attackerView.updatePosition(position);
        Block b = this.getBlock();
        if (b != null && b != currentBlock) {
            if (currentBlock != null) {
                currentBlock.removeAttacker(this);
            }
            currentBlock = b;
            currentBlock.addAttacker(this);
        }
    }

    public Board getBoard() {
        return board;
    }

    //on pourra appeller cette méthode dans wave et avant de recommencer
    //une vague on ajoute aux attackers des healthpoints
    public void setHealthPointAttackers(LinkedList<Attacker> attackers, int hwave) {
        for (Attacker attacker : attackers) {
            attacker.setHealthPoint(hwave);
        }
    }

    public void healMe(int h) {
        this.healthPoint += h;
        if (healthPoint > maxHealthPoint) {
            healthPoint = maxHealthPoint;
        }
        attackerView.healMe((double) healthPoint / maxHealthPoint, h);
    }

    private void remove() {
        attackerView.removeText();
        attackerView.removeOfBoard();
        board.addToRemove(this);
        currentBlock.removeAttacker(this);
    }

    public boolean resetRoad() {
        LinkedList<Point> tmpRoad = board.findPath(this);
        if (tmpRoad == null) {
            return false;
        } else {
            road = tmpRoad;
            road.removeFirst();
            road.addFirst(position);
            return true;
        }
    }

    public void printList(LinkedList<Point> list) {
        for (Point point : list) {
            System.out.println("towerdefense.model.attacker.Attacker.printList()");
            System.out.println("X : " + point.getX() + " Y : " + point.getY());
        }
    }

    public void move() {//pour aller de la position initiale de lattcaker au premier point de la liste
        Point p = road.getFirst();
        if (p.distance(position) <= speed) {
            road.removeFirst();
        }
        if (road.isEmpty()) {
            board.getPlayer().takeDamage(damage);
            remove();
        } else {
            this.moveToNextPoint();
        }
    }

    private void moveToNextPoint() {//pourr aller de this a un autre point
        Point newVect = position.add(getDirectionBySpeed());
        setPosition(newVect);
    }

    public Point getDirectionBySpeed() {
        Point p = road.getFirst();
        Point vecteur = new Point((p.getX() - position.getX()), (p.getY() - position.getY()));
        double X = vecteur.getX();
        double Y = vecteur.getY();
        double normeV = Math.sqrt(X * X + Y * Y);
        double facteur = speed / normeV;
        return vecteur.multiply(facteur);
    }

    public void resize(double factor) {
        position = new Point(position.getX() * factor, position.getY() * factor);
        speed = speed * factor;
        attackerView.updatePosition(position);
        attackerView.resize(factor);
        resetRoad();
    }

    public void resetAttackerView() {
        attackerView = new AttackerView(position, size, board.getBoardView(), colorN.getColor());
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public int getHealthPoint() {
        return healthPoint;
    }

    public int getCoefficient() {
        return (int) (maxHealthPoint / 100 + 5 * damage + 2 * speed);
    }

    public void setRoad(LinkedList<Point> road) {
        this.road = road;
        road.removeFirst();
        road.addFirst(position);
    }

    public Block getBlock() {
        return board.getBlockOf(position);
    }

    public int getTaille() {
        return size;
    }

    public AttackerView getAttackerView() {
        return attackerView;
    }

    public Point getPosition() {
        return position;
    }

    public void setHealthPoint(int h) {
        healthPoint += h;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public int getBlockDistanceToSpawn() {
        return road.size();
    }

    public void setVenomDamage(int damage) {
        if (damage > venomDamage) {
            venomDamage = damage;
            firstVenomDamageTime = System.currentTimeMillis();
            lastVenomDamageTime = firstVenomDamageTime;
        }
    }
}
