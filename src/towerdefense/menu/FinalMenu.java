/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import basicobject.StageSave;
import towerdefense.model.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import towerdefense.controller.Main;

/**
 *
 * @author dduong
 */
public class FinalMenu extends HBox {

    private Player player;
    private Screen screen;
    private final Main main;
    private HighScoreList highScoreList;

    private KeyBoard keyBoard;
    private HighScore hscore;
    //private MenuButton retour, quit;

    private StageSave stageSave;
    private Label GO;
    private final VBox scoreBox = new VBox(5);
    private final VBox keyboardScreen = new VBox(5);

    private final MenuButton save = new MenuButton("ENREGISTRER");
    private final MenuButton retour = new MenuButton("MENU PRINCIPAL");
    private final MenuButton quit = new MenuButton("QUITTER");

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public FinalMenu(Main main, Player player) {
        super(20);
        this.main = main;
        this.player = player;
        this.screen = new Screen();
        highScoreList = main.loadScore();

        hscore = new HighScore("", player.getScore());

        if (highScoreList.i_insert(hscore) < 10) {
            initHighScoreSaver();
        } else {
            initFinalMenu();
        }
    }

    public FinalMenu(Main main) {
        this.main = main;
        initFinalMenu();
    }

    public void initScoreBoxForSaver() {
        Region spacerVBox1 = new Region();
        VBox.setVgrow(spacerVBox1, Priority.ALWAYS);
        Region spacerVBox2 = new Region();
        VBox.setVgrow(spacerVBox2, Priority.ALWAYS);
        GridPane grid = main.getScoreView();
        grid.setAlignment(Pos.CENTER);
        scoreBox.getChildren().addAll(spacerVBox1, grid, save, spacerVBox2);
    }

    public void initScoreBoxForFinalMenu() {
        Region spacerVBox1 = new Region();
        VBox.setVgrow(spacerVBox1, Priority.ALWAYS);
        Region spacerVBox2 = new Region();
        VBox.setVgrow(spacerVBox2, Priority.ALWAYS);

        MenuText text = new MenuText("Score : "+player.getScore(), true);
        text.redimension(30, 30);
        GridPane grid = main.getScoreView();
        grid.setAlignment(Pos.CENTER);
        scoreBox.getChildren().addAll(spacerVBox1,text, grid, retour, quit, spacerVBox2);
    }

    public void initKeyBoardScreen() {
        Region spacerVBox1 = new Region();
        VBox.setVgrow(spacerVBox1, Priority.ALWAYS);
        Region spacerVBox2 = new Region();
        VBox.setVgrow(spacerVBox2, Priority.ALWAYS);
        MenuText text = new MenuText("Score : "+player.getScore(), true);
        text.redimension(50, 50);
        keyBoard = new KeyBoard(screen,this);

        keyboardScreen.getChildren().addAll(spacerVBox1, text, screen, keyBoard, spacerVBox2);

    }

    public void initHighScoreSaver() {
        Region spacerHBox1 = new Region();
        HBox.setHgrow(spacerHBox1, Priority.ALWAYS);
        Region spacerHBox2 = new Region();
        HBox.setHgrow(spacerHBox2, Priority.ALWAYS);
        Region spacerHBox3 = new Region();
        HBox.setHgrow(spacerHBox3, Priority.ALWAYS);

        initKeyBoardScreen();
        initScoreBoxForSaver();

        getChildren().addAll(spacerHBox1, keyboardScreen, spacerHBox2, scoreBox, spacerHBox3);

        save.setOnMouseClicked(event -> {
            save();
        });
    }
    
    public void save(){
        saveScore();
        main.backToMenu();
    }

    public void initFinalMenu() {
        Region spacerHBox1 = new Region();
        HBox.setHgrow(spacerHBox1, Priority.ALWAYS);
        Region spacerHBox2 = new Region();
        HBox.setHgrow(spacerHBox2, Priority.ALWAYS);

        initScoreBoxForFinalMenu();
        retour.setOnMouseClicked(event -> {
            this.main.backToMenu();
        });
        quit.setOnMouseClicked(event -> System.exit(0));
        getChildren().addAll(spacerHBox1, scoreBox, spacerHBox2);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void saveScore() {
        hscore = new HighScore(screen.getName(), player.getScore());
        highScoreList.insertScore(hscore);
        try {
            FileOutputStream saveFile = new FileOutputStream("src/backups/Score.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(highScoreList);
            out.close();
            saveFile.close();
            System.out.printf("Serialized data is saved in ../backups/Score.ser");
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public void saveForLevels(StageSave stageSave) {
        this.stageSave = stageSave;
        save.setOnMouseClicked(event -> {
            stageSave.setHightScore(player.getScore());
        });
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public KeyBoard getKB() {
        return keyBoard;
    }
}
