/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import towerdefense.model.Block;

/**
 *
 * @author tristan
 */
public class CastleView extends Rectangle {

    protected BoardView boardView;
    private Dimension dimension;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public CastleView(Point position, Dimension dimension, Dimension blockDimension, BoardView boardView) {
        super(position.getX(), position.getY(),
                dimension.getWidth() * Block.getSize(),
                dimension.getHeight() * Block.getSize());
        this.dimension = dimension;
        this.setFill(Color.RED);
        this.setArcHeight(blockDimension.getHeight() / 2);
        this.setArcWidth(blockDimension.getWidth() / 2);
        this.boardView = boardView;
    }

    public void addToBoard() {
        boardView.addView(this);
    }

    public void removeOfBoard() {
        boardView.removeView(this);
    }

    public void resize(Block block) {
//        boolean isIn = boardView.removeView(this);
        this.setX(block.X * Block.getSize());
        this.setY(block.Y * Block.getSize());
        setWidth(dimension.getWidth() * Block.getSize());
        setHeight(dimension.getHeight() * Block.getSize());
        this.setArcHeight(Block.getSize() / 2);
        this.setArcWidth(Block.getSize() / 2);
//        if (isIn) {
//            boardView.addView(this);
//        }
    }

}
