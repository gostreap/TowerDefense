/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import towerdefense.model.Block;
import towerdefense.model.Player;
import towerdefense.model.element.Element;
import towerdefense.model.element.Upgradable;
import towerdefense.model.element.Wall;

/**
 *
 * @author tristan
 */
public class ElementView extends Rectangle {

    protected BoardView boardView;
    protected Dimension dimension;
    protected transient Color color;
    protected UpgradePane upgradePane;

    protected Player player;
    protected Element element;
    private Circle range;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public ElementView(Point position, Dimension dimension,
            Dimension blockDimension, BoardView boardView, Color color, Player p,
            Element element) {
        super(position.getX(), position.getY(),
                dimension.getWidth() * blockDimension.getWidth(),
                dimension.getHeight() * blockDimension.getHeight());
        this.player = p;
        this.element = element;
        this.boardView = boardView;
        this.setArcHeight(Block.getSize() / 2);
        this.setArcWidth(Block.getSize() / 2);
        this.dimension = dimension;
        this.setFill(color);
        this.color = color;
        initRange();
        if (element instanceof Upgradable) {
            setListeners();
        }
    }

    private void initRange() {
        range = getRangeCircle();
        hideRange();
    }

    private void setListeners() {
        setOnMouseClicked(event -> {
            addUpgradePane(getX(), getY());
        });
        setOnMouseEntered(event -> setCursor(Cursor.HAND));
        setOnMouseExited(event -> setCursor(Cursor.DEFAULT));
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void addToBoard() {
        boardView.addView(range);
        boardView.addView(this);
    }

    public void removeOfBoard() {
        if (upgradePane != null) {
            boardView.removeView(upgradePane);
        }
        boardView.removeView(this);
        boardView.removeView(range);
    }

    public void removeListener() {
        setOnMouseClicked(null);
        setOnMouseEntered(null);
        setOnMouseExited(null);
    }

    public void addUpgradePane(double X, double Y) {
        boardView.closeAllUpgradePane();
        showRange();
        if (upgradePane == null) {
            upgradePane = new UpgradePane(element, this, player);
        }
        boardView.addView(upgradePane);
        boardView.layout();
        updateTranslateUpgradePane(X, Y);
    }

    public void removeUpgradePane() {
        if (upgradePane != null) {
            boardView.removeView(upgradePane);
            hideRange();
        }
    }

    public void setDimension(Dimension dim) {
        dimension = dim;
        setWidth(dimension.getWidth() * Block.getSize());
        setHeight(dimension.getHeight() * Block.getSize());
    }

    public void hideRange() {
        range.setVisible(false);
    }

    public void showRange() {
        if (!(element instanceof Wall)) {
            updateRange();
            updateRangePosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
            range.setVisible(true);
            range.toBack();
        }
    }

    public void updateRange() {
        if (range != null) {
            range.setRadius(element.getRange() * Block.getSize());
        }
    }

    public void updateUpgradePane() {
        if (upgradePane != null) {
            upgradePane.update_Pane();
        }
    }

    public void updateRangePosition(double X, double Y) {
        range.setCenterX(X);
        range.setCenterY(Y);
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resize(Block block) {
        boolean isIn = boardView.removeView(this);
        if (block != null) {
            this.setX(block.X * Block.getSize());
            this.setY(block.Y * Block.getSize());
        }
        setWidth(dimension.getWidth() * Block.getSize());
        setHeight(dimension.getHeight() * Block.getSize());
        this.setArcHeight(Block.getSize() / 2);
        this.setArcWidth(Block.getSize() / 2);
        if (upgradePane != null) {
            boardView.layout();
            updateTranslateUpgradePane(getX(), getY());
            resizeRange(getX(), getY());
        }
        if (isIn) {
            boardView.addView(this);
        }
    }

    public void resizeRange(double X, double Y) {
        updateRange();
        updateRangePosition(X + getWidth() / 2, Y + getHeight() / 2);
    }

    public void updateTranslateUpgradePane(double X, double Y) {
        if (boardView.getWidth() <= X + upgradePane.getWidth() / 2 + getWidth() / 2) {
            upgradePane.setTranslateX(boardView.getWidth() - upgradePane.getWidth());
        } else if (X - upgradePane.getWidth() / 2 + getWidth() / 2 < 0) {
            upgradePane.setTranslateX(0);
        } else {
            upgradePane.setTranslateX(X - upgradePane.getWidth() / 2 + getWidth() / 2);
        }
        if (boardView.getHeight() < Y + upgradePane.getHeight() + getHeight()) {
            upgradePane.setTranslateY(Y - upgradePane.getHeight() - 10);

        } else {
            upgradePane.setTranslateY(Y + getHeight());
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    private Circle getRangeCircle() {
        Circle rangeCircle = new Circle(getX() + getWidth() / 2,
                getY() + getHeight() / 2, element.getRange() * Block.getSize());
        rangeCircle.setFill(Color.CORNFLOWERBLUE);
        rangeCircle.setOpacity(0.3);
        return rangeCircle;
    }

    public Element getElement() {
        return element;
    }

}
