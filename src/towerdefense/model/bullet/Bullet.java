/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.bullet;

import java.util.LinkedList;
import basicobject.Point;
import towerdefense.controller.SoundController;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.attacker.Attacker;
import towerdefense.model.element.Tower;
import towerdefense.view.BoardView;
import towerdefense.view.BulletView;

/**
 *
 * @author dduong
 */
public abstract class Bullet implements java.io.Serializable {

    private static final long serialVersionUID = 211L;

    protected Point direction;
    protected Point current;
    protected double range;
    protected double speed;
    protected double distance;
    protected double stepSize;
    protected transient BulletView bulletView;
    protected Tower tower;
    protected transient BoardView boardView;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Bullet(double range, Point start, Point direction, double speed,
            BoardView boardView, Tower tower) {
        this.range = range;

        this.distance = 0;
        this.current = start;
        this.speed = speed * Block.getSize() / 20;
        this.direction = direction.multiply(this.speed);
        this.stepSize = this.direction.magnitude();
        this.tower = tower;

    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public abstract void move(Board board);

    /**
     *
     * @return true si l'élément doit être retiré du plateau, false sinon
     */
    public boolean removable() {
        return distance >= range || current.getX() > boardView.getWidth() || current.getX() < 0
                || current.getY() > boardView.getHeight() || current.getY() < 0;
    }

    /**
     *
     * @param attackers
     * @return true si la balle entre en collision avec un des attaquants de la
     * liste passée en paramètre, false sinon.
     */
    public boolean collide(LinkedList<Attacker> attackers) {
        for (Attacker a : attackers) {
            if (collide(a)) {
                a.takeDamage(tower.getDamage());
                SoundController.POP();
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param attacker
     * @return true si la balle entre en collision avec l'attaquant passé en
     * paramètre, false sinon.
     */
    public boolean collide(Attacker attacker) {
        return current.distance(attacker.getPosition()) <= attacker.getTaille();
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resize(double factor) {
        current = new Point(current.getX() * factor, current.getY() * factor);
        speed *= factor;
        range *= range * factor;
        distance *= factor;
        direction = direction.multiply(factor);
        stepSize *= factor;
        bulletView.updatePosition(current);
        bulletView.resize(factor);
    }

    public abstract void resetBulletView(Board board);

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS-----------------------------------------
    --------------------------------------------------------------------------*/
    public void setSpeed(double speed) {
        this.speed = speed * Block.getSize() / 20;
    }

    public Block getBlock(Board board) {
        return board.getBlockOf(current);
    }

    /**
     *
     * @param board
     * @return la liste des attaquants présents dans les blocs adjacents au bloc
     * de la balle
     */
    protected LinkedList<Attacker> getCloseAttackers(Board board) {
        Block block = this.getBlock(board);
        if (block == null) {
            return new LinkedList<>();
        }
        LinkedList<Attacker> closeAttackers = (LinkedList<Attacker>) block.getAttackers().clone();
        int X = block.X;
        int Y = block.Y;
        if (X > 0) {
            closeAttackers.addAll(board.getBlock(Y, X - 1).getAttackers());
            if (Y > 0) {
                closeAttackers.addAll(board.getBlock(Y - 1, X - 1).getAttackers());
            }
            if (Y < board.height - 1) {
                closeAttackers.addAll(board.getBlock(Y + 1, X - 1).getAttackers());
            }
        }
        if (X < board.width - 1) {
            closeAttackers.addAll(board.getBlock(Y, X + 1).getAttackers());
            if (Y > 0) {
                closeAttackers.addAll(board.getBlock(Y - 1, X + 1).getAttackers());
            }
            if (Y < board.height - 1) {
                closeAttackers.addAll(board.getBlock(Y + 1, X + 1).getAttackers());
            }
        }
        if (Y > 0) {
            closeAttackers.addAll(board.getBlock(Y - 1, X).getAttackers());
        }
        if (Y < board.height - 1) {
            closeAttackers.addAll(board.getBlock(Y + 1, X).getAttackers());
        }
        return closeAttackers;
    }

    public BulletView getBulletView() {
        return bulletView;
    }

}
