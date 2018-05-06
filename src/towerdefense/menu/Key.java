/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author dduong
 */
public class Key extends Parent {

    public final String letter;
    private Screen screen;

    Rectangle keyBackground;
    Text keyLetter;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Key(String l, int posX, int posY, Screen sc) {
        letter = l;
        this.screen = sc;

        keyBackground = new Rectangle(25, 25, Color.BLACK);
        keyBackground.setArcHeight(10);
        keyBackground.setArcWidth(10);
        this.getChildren().add(keyBackground);

        keyLetter = new Text(letter);
        keyLetter.setFont(new Font(20));
        keyLetter.setFill(Color.WHITE);
        keyLetter.setX(5);
        keyLetter.setY(17);
        this.getChildren().add(keyLetter);

        this.setTranslateX(posX);
        this.setTranslateY(posY);

        this.setOnMouseEntered((MouseEvent me) -> {
            keyBackground.setFill(Color.DARKGREY);
        });
        this.setOnMouseExited((MouseEvent me) -> {
            keyBackground.setFill(Color.BLACK);
        });

        this.setOnMousePressed((MouseEvent me) -> {
            if ("<".equals(this.letter)) {
                delete();
            } else {
                pressed();
            }
        });
        this.setOnMouseReleased((MouseEvent me) -> {
            released();
        });
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void pressed() {
        keyBackground.setFill(Color.DARKGREY);
        this.screen.setText(letter);
    }

    public void delete() {
        keyBackground.setFill(Color.DARKGREY);
        this.screen.delete();
    }

    public void released() {
        keyBackground.setFill(Color.BLACK);
    }
}
