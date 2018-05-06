/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author dduong
 */
public class BulletLaserView extends Line {

    private final BoardView boardView;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletLaserView(Point depart, Point arrive, BoardView boardView) {
        setStartX(depart.getX());
        setStartY(depart.getY());
        setEndX(arrive.getX());
        setEndY(arrive.getY());
        setFill(Color.RED);
        this.boardView = boardView;
    }

    public void setEnd(double x, double y) {
        setEndX(x);
        setEndY(y);
    }

    public void addToBoard() {
        boardView.addView(this);
    }

    public void removeOfBoard() {
        boardView.removeView(this);
    }
}
