/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Tristan
 */
public class MenuSlider extends StackPane {

    private Text text;
    private Slider slider;
    private Rectangle rect;
    private boolean isLargeNumber = false;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public MenuSlider(String text, double min, double max, double value) {
        this(200, 30, text, min, max, value);
    }

    public MenuSlider(double width, double height, String text, double min, double max, double value) {
        VBox box = new VBox(height / 20);
        box.setAlignment(Pos.CENTER);

        slider = new Slider(min, max, value);
        slider.setBlockIncrement(100);

        this.text = new Text(text + " : " + (int) value);
        this.text.setFill(Color.WHITE);

        box.getChildren().addAll(this.text, slider);

        rect = new Rectangle(width, height);
        rect.setArcWidth(height / 4);
        rect.setArcHeight(height / 4);
        rect.setOpacity(0.7);
        rect.setFill(Color.BLACK);

        getChildren().addAll(rect, box);
        setOnMouseEntered((event) -> {
            setCursor(Cursor.HAND);
        });
        setOnMouseExited((event) -> {
            setCursor(Cursor.DEFAULT);
        });
        slider.valueProperty().addListener(e -> {
            this.text.setText(text + " : " + getValue());
        });
    }

    /*--------------------------------------------------------------------------
    -----------------------------ESIZE------------------------------------------
    --------------------------------------------------------------------------*/
    public void redimension(double width, double height) {
        text.setFont(new Font("Arial", height / 2.5));
        this.setWidth(width);
        this.setHeight(height);
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setArcWidth(height / 4);
        rect.setArcHeight(height / 4);
        slider.setPrefHeight(height / 3);
        slider.setMaxSize(width, height / 3);
    }

    /*--------------------------------------------------------------------------
    -----------------------------SETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public void setIsLargeNumber(boolean isLargeNumber) {
        this.isLargeNumber = isLargeNumber;
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public int getValue() {
        if (isLargeNumber) {
            return ((int) slider.getValue() - (int) slider.getValue() % 100);
        } else {
            return ((int) slider.getValue());
        }
    }

}
