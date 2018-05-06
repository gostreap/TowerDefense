/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.bullet;

import java.util.LinkedList;
import basicobject.Point;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.attacker.Attacker;
import towerdefense.model.element.Tower;
import towerdefense.view.BoardView;
import towerdefense.view.BulletSniperView;

/**
 *
 * @author dduong
 */
public class BulletSniper extends Bullet implements java.io.Serializable {

    private static final long serialVersionUID = 213L;

    private static final double SPEEDCOEF = 10;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletSniper(double range, Point start, Point direction,
            BoardView boardView, Tower tower) {
        super(range, start, direction, SPEEDCOEF, boardView, tower);
        this.boardView = boardView;
        bulletView = new BulletSniperView(boardView);
        bulletView.addToBoard();
    }

    /*--------------------------------------------------------------------------
    -------------------------------METHODS--------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void move(Board board) {
        LinkedList<Attacker> listAttacker = getCloseAttackers(board);
        if (!removable() && !collide(listAttacker)) {
            current = current.add(direction);
            distance += stepSize;
            bulletView.updatePosition(current);
        } else {
            bulletView.removeOfBoard();
            tower.removeBullet(this);
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void resetBulletView(Board board) {
        boardView = board.getBoardView();
        bulletView = new BulletSniperView(boardView);
        bulletView.addToBoard();
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS-----------------------------------------
    --------------------------------------------------------------------------*/
    public static double getSpeed() {
        return SPEEDCOEF * Block.getSize() / 20;
    }
}
