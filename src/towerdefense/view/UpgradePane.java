/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import towerdefense.model.Block;
import towerdefense.model.Player;
import towerdefense.model.element.Element;
import towerdefense.model.element.Tower;
import towerdefense.model.element.Upgradable;

/**
 *
 * @author omar
 */
public class UpgradePane extends BorderPane {

    private final ElementView elementView;
    private final Element element;
    private Text name_of_tower = new Text();
    private final Player player;
    private VBox stat_VBox;
    private Button switchModeButton;

    private StackPane sellPane = new StackPane();

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public UpgradePane(Element element, ElementView elementView, Player p) {
        //setPrefWidth(230);
        //setWidth(100);

        double radius = Block.getSize() / 3;
        this.setStyle("-fx-background-color : #E8E0D199; "
                + "-fx-background-radius: " + radius + ";");

        this.elementView = elementView;
        this.element = element;
        this.player = p;
        this.switchModeButton = new Button(element.getModeString());
        initPane();

    }

    private void initPane() {
        CloseButton close_pane = new CloseButton();
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox box = new HBox(5);
        box.setPadding(new Insets(8));
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(spacer2, name_of_tower, spacer1, close_pane);
        setTop(box);

        stat_VBox = new VBox(5);
        stat_VBox.setAlignment(Pos.CENTER);
        setCenter(stat_VBox);

        Image bin = new Image("file:src/images/garbage.png");
        ImageView binImage = new ImageView(bin);
        Rectangle rect = new Rectangle(bin.getWidth(), bin.getHeight());
        rect.setFill(Color.TRANSPARENT);
        rect.setCursor(Cursor.HAND);

        rect.setOnMousePressed(event -> element.remove());

        sellPane.getChildren().addAll(binImage, rect);

        update();

        switchModeButton.setOnAction(e -> {
            element.increaseFindMode();
            switchModeButton.setText(element.getModeString());
        });

    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void layout_test() {
        for (Node node : stat_VBox.getChildren()) {
            if (node instanceof HBox) {
                ((HBox) node).layout();
            }
            if (node instanceof VBox) {
                ((HBox) node).layout();
                for (Node no : ((VBox) node).getChildren()) {
                    if (no instanceof HBox) {
                        ((HBox) no).layout();
                    }
                }
            }
        }
    }

    public void close() {
        elementView.removeUpgradePane();
    }

    public void update_Pane() {
        stat_VBox.getChildren().clear();
        update();
    }

    public void update() {
        String title = element.getElementName();
        name_of_tower.setText(title);

        LinkedList<String[]> stat_LinkedList = ((Upgradable) element).getStatList();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < stat_LinkedList.size(); i++) {
            Polygon btn_upgrade = getArrow();
            btn_upgrade.setFill(Color.web("20b54d"));

            btn_upgrade.setCursor(Cursor.HAND);
            btn_upgrade.setOnMouseExited(event -> {
                btn_upgrade.setFill(Color.web("20b54d"));
            });
            btn_upgrade.setOnMouseEntered(event -> {
                btn_upgrade.setFill(Color.LAWNGREEN);
            });
            int stat = i;
            btn_upgrade.setOnMouseClicked(event -> {
                ((Upgradable) element).upgrade(player, stat);
                update_Pane();
                elementView.updateRange();
            });
            String[] tabStrings = stat_LinkedList.get(i);
            if (tabStrings[0].equals("Dégâts") && ((Tower) element).getBoost_from_towerboost() != 0) {
                grid.add(new Text(tabStrings[0] + " : "), 0, i);
                HBox hbox1 = new HBox();
                HBox hbox2 = new HBox();
                hbox1.setAlignment(Pos.BASELINE_CENTER);
                hbox2.setAlignment(Pos.BASELINE_CENTER);
                String boost = Integer.
                        toString(((Tower) element).getBoost_from_towerboost());
                Text text1 = new Text("+ " + boost);
                text1.setFill(Color.GREEN);
                hbox1.getChildren().addAll(new Text(tabStrings[1]), text1);
                grid.add(hbox1, 1, i);

                Text text2 = new Text("+ " + boost);
                text2.setFill(Color.GREEN);
                hbox2.getChildren().addAll(new Text(tabStrings[2]), text2);

                grid.add(hbox2, 2, i);

            } else {
                grid.add(new Text(tabStrings[0] + " : "), 0, i);
                grid.add(new Text(tabStrings[1]), 1, i);
                grid.add(new Text(tabStrings[2]), 2, i);

            }
            grid.add(new Text(tabStrings[3]), 3, i);
            grid.add(btn_upgrade, 4, i);
        }
        stat_VBox.getChildren().add(0, grid);

        HBox bottom = new HBox();

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        bottom.getChildren().addAll(switchModeButton, spacer1, sellPane);
        stat_VBox.getChildren().add(bottom);
    }

    public Polygon getArrow() {
        return new Polygon(0, 0, 0, -10, -5, -10, 5, -20,
                15, -10, 10, -10, 10, 0);
    }

    /*--------------------------------------------------------------------------
    -----------------------------DEBUG------------------------------------------
    --------------------------------------------------------------------------*/
    public void printWidth() {
        for (Node node : getChildren()) {
            System.out.print(node.getClass() + " : ");
            if (node instanceof VBox) {
                for (Node no : ((VBox) node).getChildren()) {
                    System.out.println(((HBox) no).getWidth());
                }
            }
        }
    }

    /*--------------------------------------------------------------------------
    ----------------------------INNER CLASSES-----------------------------------
    --------------------------------------------------------------------------*/
    private class CloseButton extends StackPane {

        public CloseButton() {
            setPrefSize(20, 20);
            Rectangle rect1 = new Rectangle(2, 12);
            Rectangle rect2 = new Rectangle(2, 12);
            rect1.setRotate(45);
            rect2.setRotate(-45);
            Shape sh = Shape.union(rect1, rect2);
            sh.setFill(Color.GREY);
            getChildren().addAll(sh);
            setOnMouseEntered(event -> {
                setStyle("-fx-background-color: rgba(255,0,0,0.6);"
                        + "-fx-background-radius: 5em;");
                sh.setFill(Color.WHITE);
            });
            setOnMouseExited(event -> {
                setStyle("-fx-background-radius: 5em;");
                sh.setFill(Color.GREY);
            });

            setOnMouseClicked(event -> close());

        }
    }
}
