/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import static javafx.scene.shape.Shape.union;
import javafx.scene.transform.Translate;
import towerdefense.model.Block;
import towerdefense.model.Player;
import towerdefense.model.element.Element;

/**
 *
 * @author tristan
 */
public class TowerView extends ElementView {

    private double angle = 0;
    private Shape canoonShape;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public TowerView(Point position, Dimension dimension, Dimension blockDimension,
            BoardView boardView, Color color, Player p, Element element) {
        super(position, dimension, blockDimension, boardView, color, p, element);
        double X = position.getX() + (dimension.getWidth() * Block.getSize() / 2);
        double Y = position.getY() + (dimension.getHeight() * Block.getSize() / 2);
        setOpacity(0.7);

        setCanoonShape(X, Y, color);
        canoonShape.setOnMouseClicked(event -> addUpgradePane(getX(), getY()));
        canoonShape.setOnMouseEntered(event -> setCursor(Cursor.HAND));

    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    private void movePivot(Node node, double x, double y) {
        node.getTransforms().add(new Translate(-x, -y));
        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    public void rotate(double degree) {
        angle += degree;
        canoonShape.setRotate(angle);
    }

    @Override
    public void addToBoard() {
        super.addToBoard();
        boardView.addView(canoonShape);
    }

    @Override
    public void removeOfBoard() {
        super.removeOfBoard();
        boardView.removeView(canoonShape);
    }

    @Override
    public void removeListener() {
        super.removeListener();
        canoonShape.setOnMouseClicked(null);
        canoonShape.setOnMouseEntered(null);
    }

    /*--------------------------------------------------------------------------
    -----------------------------SETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    private void setCanoonShape(double X, double Y, Color color) {
        double height_canoon;
        double rayon;
        String colorString = color.toString();
        boolean move = true;
        Object[] quadruplet = getShapeAndSize(X, Y, colorString,
                this.dimension.getHeight());
        if (quadruplet != null) {
            canoonShape = (Shape) quadruplet[0];
            rayon = (double) quadruplet[1];
            height_canoon = (double) quadruplet[2];
            move = (boolean) quadruplet[3];
            canoonShape.setSmooth(true);
            canoonShape.setFill(color);
            if (move) {
                movePivot(canoonShape, 0, -height_canoon / 2 + rayon / 2);
            }
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
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

        // On s'occupe mainteant du canon
        boardView.removeView(canoonShape);
        double X = this.getX() + (dimension.getWidth() * Block.getSize() / 2);
        double Y = this.getY() + (dimension.getHeight() * Block.getSize() / 2);
        setCanoonShape(X, Y, color);
        canoonShape.setRotate(angle);
        boardView.addView(canoonShape);
        if (upgradePane != null) {
            boardView.layout();
            updateTranslateUpgradePane(getX(), getY());
            resizeRange(getX(), getY());
        }
        if (isIn) {
            boardView.addView(this);
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public double getAngle() {
        return angle;
    }

    public static Object[] getShapeAndSize(double X, double Y,
            String colorString, double size) {
        double width_canoon;
        double height_canoon;
        double rayon;
        double dimUnit = Block.getSize();
        Shape shape;
        switch (colorString) {
            case "0x00bfffff": //Air Tower
                width_canoon = dimUnit * (size / 12);
                height_canoon = dimUnit * size / 2;
                rayon = dimUnit * (size * 0.25);
                Circle circle = new Circle(X, Y, rayon);
                Rectangle canoon1 = new Rectangle(X - 5 * size * dimUnit / 36, Y, width_canoon, height_canoon);
                Rectangle canoon2 = new Rectangle(X + size * dimUnit / 18, Y, width_canoon, height_canoon);
                shape = union(circle, canoon1);
                Object[] tab1 = {union(shape, canoon2), rayon, height_canoon, true};
                return tab1;
            case "0x008000ff": //Sniper Tower
                width_canoon = dimUnit * (size / 6);
                height_canoon = dimUnit * size * 0.6;
                rayon = dimUnit * (size / 6);
                circle = new Circle(X, Y, rayon);
                canoon1 = new Rectangle(X - width_canoon / 2, Y, width_canoon, height_canoon);
                Object[] tab2 = {union(circle, canoon1), rayon, height_canoon, true};
                return tab2;
            case "0xff8c00ff": //MultiTarget Tower
                width_canoon = dimUnit * size / 4;
                height_canoon = dimUnit * size / 2;
                rayon = dimUnit * size / 4;
                circle = new Circle(X, Y, rayon);
                Polygon canoon = new Polygon(X - width_canoon / 2, Y,
                        X - width_canoon, Y + height_canoon,
                        X + width_canoon, Y + height_canoon,
                        X + width_canoon / 2, Y);
                Object[] tab3 = {union(circle, canoon), rayon, height_canoon, true};
                return tab3;
            case "0xdc143cff": //Bomb Tower
                width_canoon = dimUnit * size / 4;
                height_canoon = dimUnit * size / 2;
                rayon = dimUnit * size / 4;
                circle = new Circle(X, Y, rayon);
                canoon1 = new Rectangle(X - width_canoon / 2, Y, width_canoon, height_canoon);
                Object[] tab4 = {union(circle, canoon1), rayon, height_canoon, true};
                return tab4;
            case "0x6495edff": //Basic Tower
                width_canoon = dimUnit * size / 4;
                height_canoon = dimUnit * size / 2;
                rayon = dimUnit * size / 4;
                circle = new Circle(X, Y, rayon);
                canoon1 = new Rectangle(X - width_canoon / 2, Y, width_canoon, height_canoon);
                Object[] tab5 = {union(circle, canoon1), rayon, height_canoon, true};
                return tab5;
            case "0x008b8bff":
                width_canoon = 0;
                height_canoon = 0;
                rayon = dimUnit * (size / 2.6);
                Circle circle1 = new Circle(X, Y, rayon);
                Object[] tab6 = {circle1, rayon, height_canoon, false};
                return tab6;
            case "0x2dd62aff": //Venom Tower
                width_canoon = dimUnit * size / 4;
                height_canoon = dimUnit * size / 2;
                rayon = dimUnit * size / 4;
                circle = new Circle(X, Y, rayon);
                canoon1 = new Rectangle(X - width_canoon / 2, Y, width_canoon, height_canoon);
                Object[] tab7 = {union(circle, canoon1), rayon, height_canoon, true};
                return tab7;

            default:
                return null;

        }

    }

    public static Shape getShape(String colorString, double size) {
        Object[] quadruplet = getShapeAndSize(0, 0, colorString, size);
        if (quadruplet != null) {
            return (Shape) quadruplet[0];
        }
        return null;
    }

    public Shape getAttributeShape() {
        return canoonShape;
    }

}
