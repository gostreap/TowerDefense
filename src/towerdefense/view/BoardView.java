package towerdefense.view;

import java.util.LinkedList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.view.PopUp;

public class BoardView extends Pane {

    private Player player;
    private Board board;
    private PopUp popup;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BoardView(Board board) {
        this.player = board.getPlayer();
        this.board = board;

        this.setPrefSize(board.getPixelWidth(), board.getPixelHeight());
        this.setStyle(("-fx-background-color : #FFFCF8"));
    }


    /*--------------------------------------------------------------------------
    --------------------------------METHODS-----------------------------------
    --------------------------------------------------------------------------*/
    public void addView(Node view) {
        getChildren().add(view);
    }

    public boolean removeView(Node view) {
        return getChildren().remove(view);
    }

    public void closeAllUpgradePane() {
        LinkedList<Node> removeNodes = new LinkedList<>();
        for (Node node : getChildren()) {
            if (node instanceof UpgradePane) {
                removeNodes.add(node);
            }
        }
        for (Node node : removeNodes) {
            ((UpgradePane) node).close();
        }
    }

    /*--------------------------------------------------------------------------
    ---------------------------------POP UP-----------------------------------
    --------------------------------------------------------------------------*/
    public void showMessage(String text) {
        if (popup != null) {
            removePopUp();
        }
        popup = new PopUp(text, this);
        popup.setOnMouseClicked((event) -> {
            removePopUp();
        });
        reLocatePopUp();
        this.getChildren().add(popup);
    }

    public void removePopUp() {
        if (popup != null) {
            this.getChildren().remove(popup);
            popup = null;
        }
    }

    /*--------------------------------------------------------------------------
    ---------------------------------RESIZE-----------------------------------
    --------------------------------------------------------------------------*/
    public void resizeBoard(double width, double height) {
        board.resize(width, height);
        this.setPrefHeight(board.getPixelHeight());
        this.setPrefWidth(board.getPixelWidth());
        this.setHeight(board.getPixelHeight());
        this.setWidth(board.getPixelWidth());
        if (popup != null) {
            reLocatePopUp();
        }
    }

    public void reLocatePopUp() {
        popup.redimension();
        double x = (this.getWidth() - popup.getWidth()) / 2;
        double y = this.getHeight() / 10;
        popup.setTranslateX(x);
        popup.setTranslateY(y);
    }

}
