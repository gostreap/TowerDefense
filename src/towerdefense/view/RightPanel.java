/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Dimension;
import java.util.LinkedList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.model.element.Element;

/**
 *
 * @author tristan
 */
public class RightPanel extends GridPane {

    private Board board;
    private Player player;
    private GridPane gridTower;
    private Clock clock = new Clock();

    protected int boutonType = -1;

    //private final Label infoLabel = new Label("aire d'information");
    private HBox infoLabel = new HBox(2);

    protected StackPane heartGroup = new StackPane();

    protected StackPane scoreGroup = new StackPane();

    protected StackPane moneyGroup = new StackPane();

    private final Text moneyText = new Text("Argent");
    private final Text scoreText = new Text("Score");
    private final Text healthText = new Text("Point de vie");

   
    protected Group pauseButton = new Group();
    protected ImageView image_pbouton;
    protected Image img_play;
    protected Image img_pause;
    protected Image heartImage;

    protected HBox timeButton = new HBox(10);
    int radius = 15;
    int centerY = 50;
    protected Polygon triangle1;
    protected Polygon triangle2;
    protected Polygon triangle3;

    protected LinkedList<Pane> elementViews = new LinkedList<>();
    private boolean isForStageCreator;

    private InfoPanel infoPanel;


    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public RightPanel(Player player, double prefHeight, Board board, boolean isForStageCreator) {
        this.isForStageCreator = isForStageCreator;
        this.board = board;
        this.setAlignment(Pos.CENTER);
        this.setMaxWidth(250);
        GridPane.setHgrow(this, Priority.ALWAYS);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        getColumnConstraints().add(column);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints();
        row2.setMinHeight(150);
        getRowConstraints().addAll(row1, row2);

        gridTower = new GridPane();
        gridTower.setStyle("-fx-grid-lines-visible : false; -fx-border-color : none;");
        init(2);
        resizePanel();

        ScrollPane panetop = new ScrollPane(gridTower);
        panetop.setFitToWidth(true);

        GridPane.setConstraints(panetop, 0, 0);

        VBox bottomBox = initBottomPane();
        bottomBox.setStyle("-fx-background-color : #E8E0D1");

        GridPane.setConstraints(bottomBox, 0, 1);

        panetop.setStyle("-fx-background-color: #E8E0D1; -fx-background : #E8E0D1");

        super.getChildren().add(panetop);
        super.getChildren().add(bottomBox);

        this.player = player;

        healthText.setText(player.healthToString());

        ///pour le bouton pause
        Rectangle fond_bouton = new Rectangle(50, 46, Color.TRANSPARENT);

        img_play = new Image("file:src/images/play.png");
        img_pause = new Image("file:src/images/pause.png");

        image_pbouton = new ImageView(img_play);
        image_pbouton.setFitHeight(46);
        image_pbouton.setFitWidth(50);

        pauseButton.getChildren().add(fond_bouton);
        pauseButton.getChildren().add(image_pbouton);

        pauseButton.setCursor(Cursor.HAND);

        //pour l'accélération
        triangle1 = getTimeTriangle();
        triangle1.setFill(Color.YELLOW);
        triangle2 = getTimeTriangle();
        triangle3 = getTimeTriangle();

        timeButton.getChildren().addAll(triangle1, triangle2, triangle3);
        timeButton.setCursor(Cursor.HAND);

        //pour moneyLabel
        Image coins = new Image("file:src/images/coins.png");
        ImageView coinsImageView = new ImageView(coins);
        moneyGroup.getChildren().addAll(coinsImageView, moneyText);

        //pour le coeur
        HeartImage heartImClass = new HeartImage();
        heartImClass.modifierPixelEnRouge(64);
        heartImage = heartImClass.getFXImage();

        ImageView heart = new ImageView(heartImage);
        heartGroup.getChildren().addAll(heart, healthText);

        //pour le score
        Image score = new Image("file:src/images/star.png");
        ImageView iscore = new ImageView(score);

        scoreGroup.getChildren().addAll(iscore, scoreText);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        infoLabel.getChildren().addAll(moneyGroup, spacer1, heartGroup, spacer2, scoreGroup);

        updateLabels();
    }

    private Polygon getTimeTriangle() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(getTriangleCoordinates());
        polygon.setFill(Color.WHITE);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(1);
        return polygon;
    }

    private Double[] getTriangleCoordinates() {
        Double[] coordinates = new Double[6];
        coordinates[0] = 0.0;
        coordinates[1] = 0.0;
        coordinates[2] = 0.0;
        coordinates[3] = Math.min(getWidth(), getHeight() * 50 / 100) / 6.5;
        coordinates[4] = Math.min(getWidth(), getHeight() * 50 / 100) / 8.5;
        coordinates[5] = Math.min(getWidth(), getHeight() * 50 / 100) / 13;
        return coordinates;
    }

    private VBox initBottomPane() {
        Region spacerVBox1 = new Region();
        VBox.setVgrow(spacerVBox1, Priority.ALWAYS);
        Region spacerVBox2 = new Region();
        VBox.setVgrow(spacerVBox2, Priority.ALWAYS);

        VBox bottomBox = new VBox();
        HBox hBox = new HBox();
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        hBox.getChildren().addAll(timeButton, spacer1, clock, spacer2, pauseButton);
        bottomBox.getChildren().addAll(hBox, spacerVBox1, infoLabel, spacerVBox2);
        return bottomBox;
    }

    private void addToGridPane(int i, int k) {
        double size = getWidth() / Block.getSize() / 3.5;
        if (i % k == 0) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight((size + 0.5) * Block.getSize());
            row.setMaxHeight((size + 0.5) * Block.getSize());
            gridTower.getRowConstraints().add(row);
        }
        Pane elementView = getView(i, size);
        elementViews.add(elementView);

        elementView.setOnMouseClicked((e) -> {
            this.boutonType = i;
        });
        elementView.setOnMouseEntered(event -> {
            Element element = GameView.getElementById(i, new Block(-10, -10),
                    board, new Dimension(2.5, 2.5), isForStageCreator);
            infoPanel = new InfoPanel(element);
            gridTower.add(infoPanel, 0, (k == 2) ? 5 : 4, k, 1);
            setCursor(Cursor.HAND);
        });
        elementView.setOnMouseExited(event -> {
            gridTower.getChildren().remove(infoPanel);
            infoPanel = null;
            setCursor(Cursor.DEFAULT);
        });

        gridTower.add(elementView, i % k, i / k);
    }

    private StackPane getView(int id, double size) {
        StackPane view = new StackPane();
        view.setAlignment(Pos.CENTER);
        Element element = GameView.getElementById(id, new Block(-10, -10),
                board, new Dimension(size, size), isForStageCreator);
        ElementView elementView = element.getElementView();
        elementView.setOpacity(0.5);
        elementView.removeListener();

        view.getChildren().add(elementView);

        if (elementView instanceof TowerView) {
            Shape canoon = ((TowerView) elementView).getAttributeShape();
            view.getChildren().add(canoon);
        }
        return view;
    }

    private void init(int n) {
        for (int i = 0; i < n; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHalignment(HPos.CENTER);
            column.setPercentWidth((double) 100 / n);
            gridTower.getColumnConstraints().add(column);
        }
    }
    
    public void setImagePause(){
        image_pbouton.setImage(img_pause);
    }
    
    public void updateLabels() {
        updateMoneyLabel();
        updateScoreLabel();
        updateHealthLabel();
    }

    public void updateMoneyLabel() {
        moneyText.setText(player.moneyToString());
        moneyText.setTextAlignment(TextAlignment.CENTER);
    }

    public void updateScoreLabel() {
        scoreText.setText(player.scoreToString());
        scoreText.setTextAlignment(TextAlignment.CENTER);
    }

    public void updateHealthLabel() {
        healthText.setText(player.healthToString());
        healthText.setTextAlignment(TextAlignment.CENTER);
    }

    public void updateTimeFromBegining() {
        clock.updateTimeFromBegining();
    }

    public void updateTimePreviousPause() {
        clock.updateTimePreviousPause();
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resizePanel() {
        this.getChildren().remove(gridTower);
        for (Pane pane : elementViews) {
            pane.setOnMouseEntered(null);
            pane.setOnMouseExited(null);
        }
        elementViews.clear();
        gridTower.getChildren().clear();
        gridTower.getRowConstraints().clear();
        gridTower.getColumnConstraints().clear();

        int nbColumn = 2;
        if (getHeight() < 670) {
            nbColumn = 3;
        }
        init(nbColumn);
        int nb_element = 9;
        if (isForStageCreator) {
            nb_element = 3;
        }
        for (int i = 0; i < nb_element; i++) {
            addToGridPane(i, nbColumn);
        }
    }

    public void resizeButtons() {
        triangle1.getPoints().clear();
        triangle1.getPoints().addAll(getTriangleCoordinates());
        triangle2.getPoints().clear();
        triangle2.getPoints().addAll(getTriangleCoordinates());
        triangle3.getPoints().clear();
        triangle3.getPoints().addAll(getTriangleCoordinates());
        timeButton.setSpacing(getWidth() / 50);
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public ImageView getImage_pbouton() {
        return image_pbouton;
    }

    public Image getImg_pause() {
        return img_pause;
    }

    public Image getImg_play() {
        return img_play;
    }

    public void updateLife(double percent) {
        heartGroup.getChildren().clear();

        HeartImage heartImClass = new HeartImage();
        heartImClass.updateColor(percent);

        heartImage = heartImClass.getFXImage();
        ImageView heart = new ImageView(heartImage);
        heart.setSmooth(true);

        heartGroup.getChildren().addAll(heart, healthText);
    }
}
