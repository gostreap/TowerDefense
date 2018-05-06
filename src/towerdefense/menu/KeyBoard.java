/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import java.util.Arrays;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author dduong
 */
public class KeyBoard extends Parent {

    private Key[] keys;
    private Screen screen;
    private FinalMenu finalMenu;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public KeyBoard(Screen screen, FinalMenu finalMenu) {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String[] letters1 = {"A", "Z", "E", "R", "T", "Y", "U", "I", "O", "P"};
        String[] letters2 = {"Q", "S", "D", "F", "G", "H", "J", "K", "L", "M"};
        String[] letters3 = {"W", "X", "C", "V", "B", "N"};
        int[] posX = new int[10];
        for (int i = 0; i < posX.length; i++) {
            posX[i] = 30 * (i + 1);
        }
        this.screen = screen;
        this.finalMenu = finalMenu;

        Rectangle keyboard = new Rectangle();
        keyboard.setWidth(400);
        keyboard.setHeight(200);
        keyboard.setArcWidth(30);
        keyboard.setArcHeight(30);
        keyboard.setFill(Color.TRANSPARENT);

        keys = new Key[36];
        int j = 0;
        for (int i = 0; i < numbers.length; i++) {
            keys[j] = new Key(numbers[i], posX[i], 10, screen);
            j++;
        }
        for (int i = 0; i < letters1.length; i++) {
            keys[j] = new Key(letters1[i], posX[i], 60, screen);
            j++;
        }
        for (int i = 0; i < letters2.length; i++) {
            keys[j] = new Key(letters2[i], posX[i], 110, screen);
            j++;
        }
        for (int i = 0; i < letters3.length; i++) {
            keys[j] = new Key(letters3[i], posX[i], 160, screen);
            j++;
        }

        Key delete = new Key("<", 300, 160, screen);

        this.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.BACK_SPACE) {
                delete.delete();
            }
            if (ke.getCode() == KeyCode.ENTER){
                this.finalMenu.save();
            }
            for (Key key : keys) {
                if (key.letter.equals(ke.getText().toUpperCase())) {
                    key.pressed();
                }
            }
        });
        this.setOnKeyReleased((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.BACK_SPACE) {
                delete.released();
            }
            for (Key key : keys) {
                if (key.letter.equals(ke.getText().toUpperCase())) {
                    key.released();
                }
            }

        });
        this.getChildren().add(keyboard);
        this.getChildren().addAll(Arrays.asList(keys));
        this.getChildren().add(delete);
    }
}
