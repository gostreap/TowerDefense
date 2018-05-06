/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author omar
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class MenuText extends StackPane {

    private Text text;
    private Rectangle rect;
    private boolean fitTextWidth;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public MenuText(String text) {
        this(200, 30, text, Pos.CENTER, false);
    }

    public MenuText(String text, Pos pos) {
        this(200, 30, text, pos, false);
    }

    public MenuText(String text, boolean fitTextWidth) {
        this(200, 30, text, Pos.CENTER, fitTextWidth);
    }

    public MenuText(String text, Pos pos, boolean fitTextWidth) {
        this(200, 30, text, pos, fitTextWidth);
    }

    public MenuText(double width, double height, String text, Pos pos, boolean fitTextWidth) {
        this.fitTextWidth = fitTextWidth;
        this.text = new Text(text);
        rect = new Rectangle(width, height);
        rect.setFill(Color.TRANSPARENT);
        super.getChildren().addAll(rect, this.text);
        StackPane.setAlignment(this.text, pos);
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void redimension(double width, double height) {
        text.setFont(new Font("Arial", height));
        if (fitTextWidth) {
            this.setWidth(text.getLayoutBounds().getWidth());
            rect.setWidth(text.getLayoutBounds().getWidth());
        } else {
            this.setWidth(width);
            rect.setWidth(width);
        }
        this.setHeight(height);
        rect.setHeight(height);
    }

}
