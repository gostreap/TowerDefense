/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Point;
import javafx.scene.shape.Circle;

/**
 *
 * @author tristan
 */
public abstract class BulletView extends Circle {

    protected BoardView boardView;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletView(Point position, double radius, BoardView boardView) {
        super(position.getX(), position.getY(), radius);
        this.boardView = boardView;
    }

    public void addToBoard() {
        boardView.addView(this);
    }

    public void removeOfBoard() {
        boardView.removeView(this);
    }

    public void updatePosition(Point position) {
        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    public void resize(double factor) {
        this.setRadius(this.getRadius() * factor);
    }

}
