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
import towerdefense.view.BulletBombExplosionView;
import towerdefense.view.BulletBombView;
import towerdefense.view.BulletView;

/**
 *
 * @author dduong
 */
public class BulletBomb extends Bullet implements java.io.Serializable {

    private static final long serialVersionUID = 212L;

    private static final double SPEEDCOEF = 10;

    private BulletView bulletExplosionView;
    private boolean explode = false;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletBomb(double range, Point start, Point direction, BoardView boardView, Tower tower) {
        super(range, start, direction, SPEEDCOEF, boardView, tower);
        this.boardView = boardView;
        bulletView = new BulletBombView(boardView);
        bulletView.addToBoard();
    }

    /*--------------------------------------------------------------------------
    -------------------------------METHODS--------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void move(Board board) {
        LinkedList<Attacker> listAttacker = getCloseAttackers(board);
        if (!explode && !removable() && !explosion(listAttacker)) {
            current = current.add(direction);
            distance += stepSize;
            bulletView.updatePosition(current);
        } else {
            explode = true;
            bulletView.removeOfBoard();
            if (bulletExplosionView == null) {
                bulletExplosionView = new BulletBombExplosionView(current, boardView);
                bulletExplosionView.setRadius(Block.getSize());
                bulletExplosionView.addToBoard();
            }
            if (bulletExplosionView.getRadius() < Block.getSize() * 1.5) {
                bulletExplosionView.setRadius(bulletExplosionView.getRadius() + Block.getSize() / 5);
                collide(listAttacker);
            } else {
                bulletExplosionView.removeOfBoard();
                tower.removeBullet(this);
            }
        }
    }

    public boolean explosion(LinkedList<Attacker> attackers) {
        for (Attacker a : attackers) {
            if (super.collide(a)) {
                SoundController.EXPLOSION();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collide(Attacker attacker) {
        return (current.distance(attacker.getPosition()) - bulletExplosionView.getRadius()) <= attacker.getTaille();
    }

    @Override
    public boolean collide(LinkedList<Attacker> attackers) {
        boolean touched = false;
        for (Attacker a : attackers) {
            if (collide(a)) {
                a.takeDamage(tower.getDamage());
                touched = true;
            }
        }
        return touched;
    }


    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resetBulletView(Board board) {
        boardView = board.getBoardView();
        bulletView = new BulletBombView(boardView);
        bulletView.addToBoard();
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public static double getSpeed() {
        return SPEEDCOEF * Block.getSize() / 20;
    }
}
