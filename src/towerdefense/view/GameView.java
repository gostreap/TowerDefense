/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.menu.Menu;
import towerdefense.model.Player;
import towerdefense.model.element.AirTower;
import towerdefense.model.element.BasicTower;
import towerdefense.model.element.BombTower;
import towerdefense.model.element.BoostTower;
import towerdefense.model.element.Castle;
import towerdefense.model.element.Element;
import towerdefense.model.element.MultiTargetTower;
import towerdefense.model.element.SniperTower;
import towerdefense.model.element.Spawn;
import towerdefense.model.element.SpeedReducer;
import towerdefense.model.element.VenomTower;
import towerdefense.model.element.Wall;

/**
 *
 * @author tristan
 */
public class GameView extends GridPane {

    private Player player;

    private BoardView boardView;
    private RightPanel rightPanel;

    private ElementView elementPreView;
    private Element element;
    private int sizeX = 0;
    private int sizeY = 0;
    private int lastElementId = -1;
    private boolean dragBegin = false;
    private Block lastBlockDrag = null;
    private boolean CONTROL;

//    private final GridPane menuPause = new GridPane();
//    private final VBox gamemenu;
    private Board board;

    private final Menu menuPause;
//    private final Menu menuMain;

    private boolean isForStageCreator;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public GameView(Board board) {
        this(board, false);
    }

    public GameView(Board board, boolean isForStageCreator) {
        this.isForStageCreator = isForStageCreator;

        this.board = board;

        this.player = board.getPlayer();
        boardView = board.getBoardView();
        rightPanel = new RightPanel(player, boardView.getPrefHeight(), board,
                isForStageCreator);

        player.setRightPanel(rightPanel);

        initButton(board);

        this.add(boardView, 0, 0);
        this.add(rightPanel, 1, 0);
        this.setPrefWidth(boardView.getPrefWidth() + rightPanel.getPrefWidth());
        this.setPrefHeight(boardView.getPrefHeight());

        layout();

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                CONTROL = true;
            } else if (event.getCode() == KeyCode.ESCAPE) {
                boardView.closeAllUpgradePane();
            } else if (event.getCode() == KeyCode.S) {
                if (isForStageCreator) {
                    board.getMain().saveStage();
                }
            } else if (event.getCode() == KeyCode.ENTER) {
                boardView.removePopUp();
            }
        });

        setOnKeyReleased(event -> {
            CONTROL = false;
            if (event.getCode() == KeyCode.SPACE) {
                if (element != null) {
                    element.switchDimension();
                    sizeX = (int) element.getDimension().getWidth();
                    sizeY = (int) element.getDimension().getHeight();
                }
            }
        });

        boardView.setOnMouseMoved((event) -> {
            Point pos = new Point(-100, -100);
            Block b = new Block(-1, -1);
            boolean asMove = true;
            if (rightPanel.boutonType != -1
                    && rightPanel.boutonType != lastElementId) {
                boardView.getChildren().remove(elementPreView);
                dragBegin = false;
            }
            if (rightPanel.boutonType != -1) {
                b = board.getBlockOf(new Point(event.getX(), event.getY()));
                if (b == null || b.X > board.width - sizeX
                        || b.Y > board.height - sizeY || b == lastBlockDrag) {
                    asMove = false;
                    if (elementPreView != null && b != lastBlockDrag) {
                        elementPreView.setOpacity(0);
                    }
                } else {
                    lastBlockDrag = b;
                    pos = b.getPosition();
                    if (elementPreView != null) {
                        elementPreView.setOpacity(0.7);
                    }
                }
            }
            if (!dragBegin && rightPanel.boutonType != -1) {
                dragBegin = true;
                element = getElementById(rightPanel.boutonType,
                        new Block(-10, -10), board, isForStageCreator);
                sizeX = (int) element.getDimension().getWidth();
                sizeY = (int) element.getDimension().getHeight();
                elementPreView = element.getElementView();
                elementPreView.removeListener();
                lastElementId = rightPanel.boutonType;
                boardView.getChildren().add(elementPreView);
            } else if (dragBegin) {
                boolean free = board.isSpaceFree(b, elementPreView.dimension);
                if (asMove) {
                    elementPreView.setX(pos.getX());
                    elementPreView.setY(pos.getY());
                }
                if (!free || (asMove
                        && (!board.isPathAvailable(b, elementPreView.dimension)
                        && !isForStageCreator))) {
                    elementPreView.setFill(Color.RED);
                } else if (asMove && elementPreView.getFill() == Color.RED) {
                    elementPreView.setFill(elementPreView.color);
                }
            }
        });

        boardView.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (rightPanel.boutonType != -1) {

                    Point mousePoint = new Point(event.getX(), event.getY());
                    Element element = getElementById(rightPanel.boutonType,
                            board.getBlockOf(mousePoint), board,
                            new Dimension(sizeX, sizeY), isForStageCreator);
                    if ((element instanceof Castle)) {
                        ((Castle) element).addToBoard();
                    } else if (element instanceof Spawn) {
                        board.addSpawn((Spawn) element);
                    } else {
                        if (!isForStageCreator) {
                            board.addElement(element);
                        } else {
                            board.addElementWithoutPay(element);
                        }
                    }
                    if (!CONTROL) {
                        rightPanel.boutonType = -1;
                    }
                    boardView.getChildren().remove(elementPreView);
                    elementPreView = null;
                    dragBegin = false;
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (element != null) {
                    element.switchDimension();
                    sizeX = (int) element.getDimension().getWidth();
                    sizeY = (int) element.getDimension().getHeight();
                }
            }
        });
        menuPause = new Menu(board.getMain(), boardView);
        //fclmenuPause.setMenuPause(this, isForStageCreator);
    }

    public static Element getElementById(int id, Block block, Board board,
            Dimension dimension, boolean isForStageCreator) {
        if (!isForStageCreator) {
            switch (id) {
                case 0:
                    return new AirTower(block, board, dimension);
                case 1:
                    return new BasicTower(block, board, dimension);
                case 2:
                    return new BombTower(block, board, dimension);
                case 3:
                    return new BoostTower(block, board, dimension);
                case 4:
                    return new MultiTargetTower(block, board, dimension);
                case 5:
                    return new SniperTower(block, board, dimension);
                case 6:
                    return new SpeedReducer(block, board, dimension);
                case 7:
                    return new Wall(block, board, dimension);
                case 8:
                    return new VenomTower(block, board, dimension);
                default:
                    return null;
            }
        } else {

            switch (id) {
                case 0:
                    return new Wall(block, board, dimension);
                case 1:
                    return new Spawn(block, board, dimension);
                case 2:
                    return new Castle(block, board, dimension);
                default:
                    return null;
            }
        }
    }

    public static Element getElementById(int id, Block block, Board board,
            boolean isForStageCreator) {
        if (!isForStageCreator) {
            switch (id) {
                case 0:
                    return new AirTower(block, board);
                case 1:
                    return new BasicTower(block, board);
                case 2:
                    return new BombTower(block, board);
                case 3:
                    return new BoostTower(block, board);
                case 4:
                    return new MultiTargetTower(block, board);
                case 5:
                    return new SniperTower(block, board);
                case 6:
                    return new SpeedReducer(block, board);
                case 7:
                    return new Wall(block, board);
                case 8:
                    return new VenomTower(block, board);
                default:
                    return null;
            }
        } else {
            switch (id) {
                case 0:
                    return new Wall(block, board);
                case 1:
                    return new Spawn(block, board);
                case 2:
                    return new Castle(block, board);
                default:
                    return null;
            }
        }
    }

    public void initButton(Board board) {

        rightPanel.pauseButton.setOnMouseClicked(e -> {
            if (!board.isStarted()) {
                rightPanel.image_pbouton.setImage(rightPanel.img_pause);
                rightPanel.updateTimePreviousPause();
                board.start();
            } else {
                boolean isInPause = board.togglePause();
                if (isInPause) {
                    rightPanel.image_pbouton.setImage(rightPanel.img_play);
                    menuPause.clear();
                    menuPause.setMenuPause(this, isForStageCreator, board.isAStage());
                    getChildren().add(menuPause);
                } else { //play
                    rightPanel.image_pbouton.setImage(rightPanel.img_pause);
                    getChildren().remove(menuPause);
                    rightPanel.updateTimePreviousPause();
                }
            }
        });

        rightPanel.timeButton.setOnMouseClicked(e -> {
            board.toggleTimeSpeed();
            int timeCoef = board.getTimeCoef();
            switch (timeCoef) {
                case 1:
                    rightPanel.triangle3.setFill(rightPanel.triangle1.getFill());
                    break;
                case 2:
                    rightPanel.triangle2.setFill(rightPanel.triangle1.getFill());
                    rightPanel.triangle3.setFill(Color.WHITE);
                    break;
            }
            if (timeCoef != 1 && timeCoef != 2) {
                rightPanel.triangle2.setFill(Color.WHITE);
                rightPanel.triangle3.setFill(Color.WHITE);
            }
        });
    }

    public void resizeGame(double width, double height) {
        super.resize(width, height);
        boardView.resizeBoard(width - 195, height);
        rightPanel.setMaxHeight(boardView.getPrefHeight());
        this.setMaxWidth(boardView.getPrefWidth() + rightPanel.getPrefWidth());
        this.setMaxHeight(boardView.getPrefHeight());
        menuPause.resizeButton();
        layout();
        menuPause.setTranslateY(boardView.getTranslateY()
                + boardView.getHeight() / 2 - menuPause.getHeight() / 2);
        menuPause.setSpacing(boardView.getHeight() / 200);
        rightPanel.resizePanel();
        rightPanel.resizeButtons();
        menuPause.relocateMenuPause();
    }

    public void resume() {
        rightPanel.image_pbouton.setImage(rightPanel.img_pause);
        getChildren().remove(menuPause);
        rightPanel.updateTimePreviousPause();
        board.togglePause();
    }

    public void save() {
        board.save();
    }

}
