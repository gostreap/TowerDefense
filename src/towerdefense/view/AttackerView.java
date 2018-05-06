package towerdefense.view;

import basicobject.Point;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import towerdefense.model.Block;
import towerdefense.model.attacker.*;

public class AttackerView extends Circle {

    protected transient Color attackerColor;
    protected Attacker attacker;
    protected BoardView boardView;

    protected ProgressBar health_bar = new ProgressBar(1);
    protected double hb_width = Block.getSize();
    protected double hb_height = Block.getSize() / 5;

    Text text;

    private double lastTimeGetShoot = System.currentTimeMillis();
    private double lastTimeGetHeal = System.currentTimeMillis();

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public AttackerView(Point position, int radius, BoardView boardView, Color color) {
        super(0, 0, radius);
        this.attackerColor = color;
        this.boardView = boardView;
        health_bar.setPrefSize(hb_width, hb_height);
        health_bar.setMinSize(hb_width, hb_height);
        health_bar.setMaxSize(hb_width, hb_height);
        health_bar.setStyle("-fx-accent: red;");
        this.setFill(color);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void addToBoard() {
        boardView.addView(this);
        boardView.addView(health_bar);
        this.toFront();
    }

    public void removeOfBoard() {
        boardView.removeView(this);
        boardView.removeView(health_bar);
        removeText();
    }

    public void takeDamage(double percent, int damage) {
        lastTimeGetShoot = System.currentTimeMillis();
        setFill(Color.RED);
        update_health(percent);
        showText(-damage);
    }

    public void healMe(double percent, int heal) {
        lastTimeGetHeal = System.currentTimeMillis();
        setFill(Color.PINK);
        update_health(percent);
        showText(heal);

    }

    public void removeText() {
        boardView.removeView(text);
    }

    public void showText(int damage) {
        removeText();
        if (damage < 0) {
            text = new Text(Integer.toString(damage));
            text.setFill(Color.RED);
        } else {
            text = new Text("+" + Integer.toString(damage));
            text.setFill(Color.LIMEGREEN);
        }
        text.setTranslateX(getTranslateX());
        text.setTranslateY(getTranslateY() - 2 * getRadius());
        boardView.addView(text);
        resizeText();
    }

    /*--------------------------------------------------------------------------
    -----------------------------UPDATE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void updatePosition(Point position) {
        setTranslateX(position.getX());
        setTranslateY(position.getY());
        health_bar.setTranslateX(position.getX() - hb_width / 2);
        health_bar.setTranslateY(position.getY() - getRadius() - hb_height - 2);
    }

    public void updateColor() {
        boolean b = (System.currentTimeMillis() - lastTimeGetShoot > 100
                && getFill() == Color.RED)
                || (System.currentTimeMillis() - lastTimeGetHeal > 200
                && getFill() == Color.PINK);
        if (b) {
            removeText();
            setFill(attackerColor);
        }
    }

    public void update_health(double health) {
        health_bar.setProgress(health);
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resize(double factor) {
        boolean isIn = boardView.removeView(this);
        this.setRadius(this.getRadius() * factor);
        hb_width = Block.getSize();
        hb_height = Block.getSize() / 5;
        health_bar.setPrefSize(hb_width, hb_height);
        health_bar.setMinSize(hb_width, hb_height);
        health_bar.setMaxSize(hb_width, hb_height);
        if (isIn) {
            boardView.addView(this);
        }
        resizeText();
    }

    public void resizeText() {
        if (text != null) {
            text.setFont(new Font("Arial", boardView.getHeight() / 50));
        }
    }

}
