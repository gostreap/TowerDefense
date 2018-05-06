/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author aissatou
 */
public class MenuButton extends StackPane {

    private Text text;
    private Rectangle rect;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public MenuButton(String text) {
        this(200, 30, text);
    }

    public MenuButton(double width, double height, String text) {
        this.text = new Text(text);
        this.text.setFill(Color.WHITE);
        rect = new Rectangle(width, height);
        rect.setArcWidth(height / 4);
        rect.setArcHeight(height / 4);
        rect.setOpacity(0.7);
        rect.setFill(Color.BLACK);
        super.getChildren().addAll(rect, this.text);
        setOnMouseEntered((event) -> {
            rect.setFill(Color.ORANGE);
            this.text.setFill(Color.BLACK);
            setCursor(Cursor.HAND);
        });
        setOnMouseExited((event) -> {
            rect.setFill(Color.BLACK);
            this.text.setFill(Color.WHITE);
            setCursor(Cursor.DEFAULT);
        });
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void redimension(double width, double height) {
        text.setFont(new Font("Arial", height / 2.5));
        this.setWidth(width);
        this.setHeight(height);
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setArcWidth(height / 4);
        rect.setArcHeight(height / 4);
    }

    /*--------------------------------------------------------------------------
    -----------------------------SETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public void setText(String text) {
        super.getChildren().remove(this.text);
        this.text = new Text(text);
        this.text.setFont(new Font("Arial", rect.getHeight() / 2.5));
        super.getChildren().add(this.text);
    }
}
