/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author dduong
 */
public class Screen extends Parent {

    private Text[] name;
    private int current, posX, posY;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Screen() {
        this.current = 0;
        this.posX = 20;
        this.posY = 25;
        this.name = new Text[16];
        Rectangle screen = new Rectangle();
        screen.setWidth(400);
        screen.setHeight(50);
        screen.setArcWidth(30);
        screen.setArcHeight(30);
        screen.setFill(Color.BLACK);
        getChildren().add(screen);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void nextLetter() {
        if (current < 16) {
            current++;
            posX += 20;
        }
    }

    public void delete() {
        if (current > 0) {
            current--;
            posX -= 20;
            this.getChildren().remove(name[current]);
            name[current] = null;
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------SETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public void setText(String l) {
        if (current >= 0 && current < 16) {
            name[current] = new Text(l);
            name[current].setFont(new Font(20));
            name[current].setFill(Color.WHITE);
            name[current].setX(posX);
            name[current].setY(posY);
            this.getChildren().add(name[current]);
            nextLetter();
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public String getName() {
        String res = "";
        for (Text text : name) {
            if (text != null) {
                res += text.getText();
            }
        }
        return res;
    }
}
