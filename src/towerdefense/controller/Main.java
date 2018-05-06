/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.controller;

import basicobject.ListElementPosition;
import basicobject.ListStage;
import basicobject.StageSave;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import towerdefense.model.Board;
import towerdefense.menu.HighScore;
import towerdefense.menu.HighScoreList;
import towerdefense.menu.FinalMenu;
import towerdefense.menu.Menu;
import towerdefense.view.BoardView;
import towerdefense.view.GameView;

/**
 *
 * @author tristan
 */
public class Main {

    private final Stage window;
    private final Scene scene;
    private Board board;

    private Pane mainMenu = new Pane();
    private GameView gameView;
//    private Menu gameMenu;
    private FinalMenu finalMenu;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Main(Stage window) {
        this.window = window;
        window.setMinHeight(500);
        window.setMinWidth(1000);

        scene = new Scene(mainMenu);
        scene.getStylesheets().add("file:src/css/progressBar.css");
        backToMenu();

        window.setScene(scene);

        window.setTitle("Packdefender");
        window.show();
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public void newGame(int width_f, int height_f, int hp, int nbSpawn,
            int nbCastle) {
        int width = width_f;
        int height = height_f;
        board = new Board(width, height, scene.getWidth(), scene.getHeight(),
                this, hp, nbSpawn, nbCastle);
        gameView = new GameView(board);
        initResizeListener();
        gameView.resizeGame(scene.getWidth(), scene.getHeight());
        scene.setRoot(gameView);
        window.setWidth(gameView.getWidth());
        window.setHeight(gameView.getHeight());
        board.play();
    }

    private void initResizeListener() {
        gameView.widthProperty().addListener(e -> {
            if (gameView != null) {
                gameView.resizeGame(gameView.getWidth(), gameView.getHeight());
            }
        });
        gameView.heightProperty().addListener(e -> {
            if (gameView != null) {
                gameView.resizeGame(gameView.getWidth(), gameView.getHeight());
            }
        });
    }

    public void launchStageCreator(int width, int height, int hp, int money) {
        board = new Board(width, height, window.getWidth(), window.getHeight(),
                this, hp, true, money);
        gameView = new GameView(board, true);
        initResizeListener();
        gameView.resizeGame(scene.getWidth(), scene.getHeight());
        scene.setRoot(gameView);
        window.setHeight(gameView.getHeight());
    }

    public void toFinalMenu() {
        this.finalMenu = new FinalMenu(this, this.board.getPlayer());
        scene.setRoot(finalMenu);
        if (this.finalMenu.getKB() != null) {
            this.finalMenu.getKB().requestFocus();
        }
    }

    public void toFinalMenu(int stageLevel) {
        ListStage listStage = loadListStage();
        listStage.updateHighScore(stageLevel, board.getPlayer().getScore());
        saveListStage(listStage);
        backToMenu();
    }

    public void toFinalMenuBis() {
        this.finalMenu = new FinalMenu(this);
        scene.setRoot(finalMenu);
    }

    public void backToMenu() {
        mainMenu = new Pane();
        Menu menu = new Menu(this);
        menu.setMainMenuConfig();
        BoardView boardView = menu.getBoardView();
        mainMenu.getChildren().add(boardView);
        mainMenu.getChildren().add(menu);
        scene.setRoot(mainMenu);

        resizeMainMenu(menu, boardView);

        mainMenu.heightProperty().addListener(e -> {
            resizeMainMenu(menu, boardView);
        });

        mainMenu.widthProperty().addListener(e -> {
            resizeMainMenu(menu, boardView);
        });
    }

    /*--------------------------------------------------------------------------
    -----------------------------SAVES AND LOAD---------------------------------
    --------------------------------------------------------------------------*/
    public boolean saveStage() {
        return saveStage(board.getStageSave());
    }

    /**
     *
     * @param stageSave
     * @return boolean if stage have been saved without problem
     */
    public boolean saveStage(StageSave stageSave) {
        ListStage listStage = loadListStage();
        if (listStage == null) {
            System.out.println("hey");
            listStage = new ListStage();
        }
        listStage.add(stageSave);

        try {
            FileOutputStream saveFile = new FileOutputStream("src/backups/Niveau.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(listStage);
            out.close();

            saveFile.close();
            System.out.printf("Serialized data is saved in ../backups/Niveau.ser");

            return true;
        } catch (IOException i) {
        }
        return false;
    }

    public void loadStage(int stage) {
        ListStage listStage = loadListStage();
        if (listStage != null && listStage.canPlay(stage)) {
            StageSave stageSave = listStage.getStage(stage);
            System.out.println("hightScore : " + stageSave.getHightScore());
            if (stageSave != null) {
                int width = stageSave.getWidth();
                int height = stageSave.getHeight();
                ListElementPosition listElementPosition
                        = stageSave.getListElementPosition();
                int hp = stageSave.getHp();
                int money = stageSave.getMoney();

                board = new Board(width, height, window.getWidth(),
                        window.getHeight(), this, hp, listElementPosition, money, stage);

                gameView = new GameView(board);
                initResizeListener();
                gameView.resizeGame(scene.getWidth(), scene.getHeight());
                scene.setRoot(gameView);
                window.setHeight(gameView.getHeight());
                board.play();
            }
        }
    }

    public void SaveGame(String backupName) {
        try {
            FileOutputStream saveFile = new FileOutputStream("src/backups/" + backupName);
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(board);
            out.close();
            saveFile.close();
            System.out.printf("Serialized data is saved in ../backups/" + backupName);
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public boolean loadGame() {
        Board board = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/backups/Partie.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("Il n'y a pas de partie sauvegardée");
            return false;
        } catch (ClassNotFoundException c) {
            System.out.println("Il n'y a pas de partie sauvegardée");
            c.printStackTrace();
            return false;
        }
        if (board != null) {
            this.board = board;
            board.setMain(this);
            board.load();
            board.togglePause();
            this.gameView = new GameView(board);
            gameView.resizeGame(scene.getWidth(), scene.getHeight());
            initResizeListener();
            scene.setRoot(gameView);
            return true;
        }
        return false;
    }

    public boolean saveListStage(ListStage listStage) {
        try {
            FileOutputStream saveFile = new FileOutputStream("src/backups/Niveau.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(listStage);
            out.close();

            saveFile.close();
            System.out.printf("Serialized data is saved in ../backups/Niveau.ser");

            return true;
        } catch (IOException i) {
        }
        return false;
    }

    public ListStage loadListStage() {
        try {
            FileInputStream fileIn = new FileInputStream("src/backups/Niveau.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ListStage listStage = (ListStage) in.readObject();
            in.close();
            fileIn.close();
            return listStage;
        } catch (Exception exception) {
            return null;
        }
    }

    public HighScoreList loadScore() {
        try {
            HighScoreList hs;
            FileInputStream fileIn = new FileInputStream("src/backups/Score.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            hs = (HighScoreList) in.readObject();
            in.close();
            fileIn.close();
            return hs;
        } catch (Exception e) {
            return new HighScoreList();
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public GridPane getScoreView() {
        HighScoreList highScoreList = loadScore();
        GridPane grid = new GridPane();
        grid.add(new Text("TOP 10"), 0, 0);
        for (int i = 1; i <= 10; i++) {
            HighScore score = highScoreList.getScore(i);
            if (score != null) {
                String name = score.getName();
                if (score.getName().equals("")) {
                    name = "SANS PSEUDO";
                }
                grid.add(new Text(name + " : "), 0, i);
                grid.add(new Text(Integer.toString(score.getScore())), 1, i);
            }
        }
        return grid;
    }

    public Pane getMainMenu() {
        return mainMenu;
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void resizeMainMenu(Menu menu, BoardView boardView) {
        boardView.resizeBoard(mainMenu.getWidth(), mainMenu.getHeight());
        boardView.setTranslateX(mainMenu.getWidth() / 2 - boardView.getWidth() / 2);
        menu.resizeButton();
        mainMenu.layout();
        menu.relocate();
    }

}
