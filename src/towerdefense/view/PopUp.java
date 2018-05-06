package towerdefense.view;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PopUp extends StackPane {

    private Text text;
    private Rectangle rect;
    private BoardView boardView;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public PopUp(String text, BoardView boardView) {
        this.boardView = boardView;

        this.text = new Text(text);
        this.text.setFill(Color.WHITE);
        rect = new Rectangle();
        rect.setOpacity(0.5);
        rect.setFill(Color.BLACK);
        super.getChildren().addAll(rect, this.text);
        setOnMouseEntered((event) -> {
            rect.setFill(Color.ORANGE);
            rect.setOpacity(0.5);
            this.text.setFill(Color.BLACK);
            setCursor(Cursor.HAND);
        });
        setOnMouseExited((event) -> {
            rect.setFill(Color.BLACK);
            rect.setOpacity(0.5);
            this.text.setFill(Color.WHITE);
            setCursor(Cursor.DEFAULT);
        });
        setOnMousePressed((event) -> {
            boardView.removePopUp();
        });
        redimension();
    }

    /*--------------------------------------------------------------------------
    ---------------------------------RESIZE-----------------------------------
    --------------------------------------------------------------------------*/
    public void redimension() {
        double width = boardView.getWidth() / 4;
        double height = boardView.getHeight() / 20;
        text.setFont(new Font("Arial", height / 2.5));
        layout();
        this.setWidth(width);
        this.setHeight(height);
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setArcWidth(height / 4);
        rect.setArcHeight(height / 4);
    }

}
