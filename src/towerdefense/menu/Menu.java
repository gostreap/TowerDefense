/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import basicobject.ListStage;
import basicobject.StageSave;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import towerdefense.controller.Main;
import towerdefense.controller.SoundController;
import towerdefense.model.Board;
import towerdefense.view.BoardView;
import towerdefense.view.GameView;

/**
 *
 * @author tristan
 */
public class Menu extends VBox {

    private Main main;

    private Board board;
    private BoardView boardView;
    private LinkedList<MenuButton> listButton = new LinkedList<>();
    private LinkedList<MenuSlider> listSliders = new LinkedList<>();
    private LinkedList<MenuText> listText = new LinkedList<>();
    private LinkedList<Text> listText2 = new LinkedList<>();
    //private VBox menuOptions = new VBox(10);

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Menu(Main main, BoardView boardView) {
        this.main = main;

        if (boardView == null) {
            loadBoardView();
        } else {
            this.boardView = boardView;
        }
    }

    public Menu(Main main) {
        this(main, null);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    /**
     * Charge le plateau et la vue plateau pour le fond du menu principal
     */
    private void loadBoardView() {
        Board board = null;
        try {
            FileInputStream fileIn = new FileInputStream(
                    "src/backups/Background.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
        if (board != null) {
            board.setMain(main);
            board.load();
            board.removeListeners();
            board.togglePause();
            board.getPlayer().setInMenu(true);
            this.board = board;
            this.boardView = board.getBoardView();
        }
    }

    /**
     * Va au menu principal
     */
    public void setMainMenuConfig() {
        clear();

        MenuButton btnjouer = new MenuButton("JOUER");
        btnjouer.setOnMouseClicked(event -> {
            setLaunchGameConfig();
            SoundController.CLICK();
        });
        listButton.add(btnjouer);

        MenuButton btnScore = new MenuButton("SCORES");
        btnScore.setOnMouseClicked(event -> {
            setScoreConfig();
            SoundController.CLICK();
        });
        listButton.add(btnScore);

        MenuButton btnCharger = new MenuButton("CHARGER LA PARTIE");
        btnCharger.setOnMouseClicked(event -> {
            boolean b = main.loadGame();
            if (b) {
                board.stop();
            } else {
                boardView.showMessage("Il n'y a pas de partie sauvegardée");
            }
            SoundController.CLICK();
        });
        listButton.add(btnCharger);

        MenuButton btnLaunchStageCreator = new MenuButton("CREATEUR DE NIVEAU");
        btnLaunchStageCreator.setOnMouseClicked(event -> {
            setCreatorStageConfig();
            SoundController.CLICK();
        });
        listButton.add(btnLaunchStageCreator);

        MenuButton btnLauchStageLoad = new MenuButton("CHARGER NIVEAU");
        btnLauchStageLoad.setOnMouseClicked(event -> {
            setLevelChooserConfig(0);
            SoundController.CLICK();
        });
        listButton.add(btnLauchStageLoad);

        MenuButton btnSounds = new MenuButton(SoundController.getText());
        btnSounds.setOnMouseClicked(event -> {
            SoundController.CLICK();
            SoundController.toggleMute();
            btnSounds.setText(SoundController.getText());
        });
        listButton.add(btnSounds);

        MenuButton btnHelp = new MenuButton("AIDE");
        btnHelp.setOnMouseClicked(event -> {
            setHelpConfig(0);
            SoundController.CLICK();
        });
        listButton.add(btnHelp);

        MenuButton btnQuit = new MenuButton("QUITTER");
        btnQuit.setOnMouseClicked(event -> {
            SoundController.CLICK();
            System.exit(0);
        });
        listButton.add(btnQuit);

        this.getChildren()
                .addAll(btnjouer, btnScore, btnCharger, btnLaunchStageCreator,
                        btnLauchStageLoad, btnSounds, btnHelp, btnQuit);
        reorganize();
    }

    /**
     * Va au menu pour configurer la partie puis la lancer
     */
    private void setLaunchGameConfig() {
        clear();

        MenuSlider sizeSlider = new MenuSlider("Taille du plateau", 35, 65, 40);
        MenuSlider hpSlider = new MenuSlider("Point de vie", 1, 199, 100);
        MenuSlider spawnSlider = new MenuSlider("Nb zones d'appariton", 1, 3, 2);
        MenuSlider castleSlider = new MenuSlider("Nombre de château", 1, 3, 2);
        listSliders.add(sizeSlider);
        listSliders.add(hpSlider);
        listSliders.add(spawnSlider);
        listSliders.add(castleSlider);

        MenuButton btn_ok = new MenuButton("LANCER LA PARTIE");
        btn_ok.setOnMousePressed(event -> {
            SoundController.CLICK();
            board.stop();
            main.newGame((int) sizeSlider.getValue(),
                    (int) sizeSlider.getValue() * 10 / 16,
                    (int) hpSlider.getValue(), (int) spawnSlider.getValue(),
                    (int) castleSlider.getValue());
        });
        listButton.add(btn_ok);

        MenuButton btn_retour_menu_principal = new MenuButton("RETOUR");
        btn_retour_menu_principal.setOnMousePressed(event -> {
            setMainMenuConfig();
            SoundController.CLICK();
        });
        listButton.add(btn_retour_menu_principal);

        this.getChildren().addAll(sizeSlider, hpSlider, spawnSlider,
                castleSlider, btn_ok, btn_retour_menu_principal);

        reorganize();
    }

    /**
     * Va au menu pour configurer les paramêtre de la partie à créer
     */
    private void setCreatorStageConfig() {
        clear();

        MenuSlider sizeSlider = new MenuSlider("Taille du plateau", 35, 65, 40);
        MenuSlider hpSlider = new MenuSlider("Point de vie", 1, 199, 100);
        MenuSlider moneySlider = new MenuSlider("Argent", 1000, 10000, 2500);
        moneySlider.setIsLargeNumber(true);
        listSliders.add(sizeSlider);
        listSliders.add(hpSlider);
        listSliders.add(moneySlider);

        MenuButton btn_ok = new MenuButton("Créer un niveau");
        btn_ok.setOnMousePressed(event -> {
            SoundController.CLICK();
            board.stop();
            main.launchStageCreator((int) sizeSlider.getValue(),
                    (int) sizeSlider.getValue() * 10 / 16,
                    (int) hpSlider.getValue(),
                    (int) moneySlider.getValue());
        });
        listButton.add(btn_ok);

        MenuButton btn_retour_menu_principal = new MenuButton("MENU PRINCIPAL");
        btn_retour_menu_principal.setOnMousePressed(event -> {
            setMainMenuConfig();
            SoundController.CLICK();
        });
        listButton.add(btn_retour_menu_principal);

        this.getChildren().addAll(sizeSlider, hpSlider, moneySlider, btn_ok,
                btn_retour_menu_principal);

        reorganize();
    }

    /**
     *
     */
    private void setLevelChooserConfig(int page) {
        ListStage listStage = main.loadListStage();
        int first_lvl_on_page = page * 4 + 1;
        int last_lvl_on_page = first_lvl_on_page + 4;
        boolean isFirstPage = page == 0;
        boolean isLastPage = last_lvl_on_page >= listStage.size();

        if (isFirstPage) {
            first_lvl_on_page = 0;
        }
        if (isFirstPage || isLastPage) {
            last_lvl_on_page = first_lvl_on_page + 5;
        }

        if (listStage != null) {
            clear();
            for (int i = first_lvl_on_page; i < last_lvl_on_page
                    && i < listStage.size(); i++) {
                MenuButton btn_lvl_i = new MenuButton("Level " + Integer.toString(i));
                int stage = i;
                btn_lvl_i.setOnMousePressed(event -> {
                    SoundController.CLICK();
                    if (event.getButton() == MouseButton.PRIMARY) {
                        board.stop();
                        main.loadStage(stage);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        setConfigStage(page, stage, listStage);
                    }
                });
                listButton.add(btn_lvl_i);
                this.getChildren().add(btn_lvl_i);
            }

            if (!isFirstPage) {
                MenuButton previous = new MenuButton("PRECEDENT");
                previous.setOnMousePressed(event -> {
                    SoundController.CLICK();
                    setLevelChooserConfig(page - 1);
                });
                listButton.add(previous);
                this.getChildren().add(previous);
            }
            if (!isLastPage) {
                MenuButton next = new MenuButton("SUIVANT");
                next.setOnMousePressed(event -> {
                    setLevelChooserConfig(page + 1);
                    SoundController.CLICK();
                });
                listButton.add(next);
                this.getChildren().add(next);
            }

            MenuButton menu_principal = new MenuButton("MENU PRINCIPAL");
            menu_principal.setOnMousePressed(e -> {
                setMainMenuConfig();
                SoundController.CLICK();
            });
            listButton.add(menu_principal);
            this.getChildren().add(menu_principal);
            reorganize();
        }
    }

    public void setConfigStage(int page, int level, ListStage listStage) {
        if (listStage != null) {
            clear();

            StageSave stageSave = listStage.getStage(level);

            MenuSlider score_need = new MenuSlider("Score requis", 0, 20000,
                    stageSave.getScoreNeed());
            listSliders.add(score_need);

            MenuButton save = new MenuButton("ENREGISTRER");
            save.setOnMousePressed(event -> {
                SoundController.CLICK();
                stageSave.setPreviousScoreNeed(score_need.getValue());
                main.saveListStage(listStage);
            });
            listButton.add(save);

            MenuButton delete = new MenuButton("SUPPRIMER NIVEAU");
            delete.setOnMousePressed(event -> {
                SoundController.CLICK();
                listStage.remove(level);
                main.saveListStage(listStage);
                setLevelChooserConfig(page);
            });
            listButton.add(delete);

            MenuButton retourButton = new MenuButton("RETOUR");
            retourButton.setOnMousePressed(event -> {
                SoundController.CLICK();
                setLevelChooserConfig(page);
            });
            listButton.add(retourButton);

            this.getChildren().addAll(score_need, delete, save, retourButton);

            reorganize();
        }
    }

    /**
     * Va au menu des highscore
     */
    private void setScoreConfig() {
        clear();

        MenuText top10 = new MenuText("TOP 10");
        listText.add(top10);
        this.getChildren().add(top10);

        HighScoreList highScoreList = main.loadScore();
        GridPane gridPane = new GridPane();
        for (int i = 0; i < 10; i++) {
            HighScore score = highScoreList.getScore(i);
            if (score != null) {
                String name = score.getName();
                if (score.getName().equals("")) {
                    name = "SANS PSEUDO";
                }
                MenuText nameText = new MenuText(name, Pos.CENTER_LEFT);
                listText.add(nameText);
                gridPane.add(nameText, 0, i);
                MenuText scoreText = new MenuText(Integer.toString(score.getScore()), Pos.CENTER_LEFT, true);
                listText.add(scoreText);
                gridPane.add(scoreText, 1, i);
            }
        }
        this.getChildren().add(gridPane);

        MenuButton menu_principal = new MenuButton("MENU PRINCIPAL");
        menu_principal.setOnMousePressed(e -> {
            SoundController.CLICK();
            setMainMenuConfig();
        });

        listButton.add(menu_principal);

        this.getChildren()
                .add(menu_principal);

        reorganize();
    }

    /**
     * Va au menu d'aide
     */
    private void setHelpConfig(int page) {
        clear();

        Text info = new Text("");
        Text[] textTab = new Text[8];

        boolean isFirst = page == 0;
        boolean isLast = textTab.length - 1 == page;

        textTab[0] = new Text("Le but du jeu est de protéger les châteaux "
                + "(en rouge) des attaquants.\nIl faut pour cela placer des tours "
                + "sur le plateau. \nChaque tour a un coût et des caractéristiques "
                + "propres.");

        textTab[1] = new Text("\"En cliquant sur jouer vous choisirez d'abord les"
                + "différentes caractéristiques de la partie avant de lancer celle ci.");

        textTab[2] = new Text("Vous commencez la partie avec une certaine somme d'argent. "
                + "Vous disposez de temps pour placer vos premières tours calmement. "
                + "Une fois cela fait, il suffit de cliquer sur le bouton 'play'.");

        textTab[3] = new Text("Pour placer les tours plus efficacement, vous pouvez maintenir \n"
                + "la touche contrôle enfoncée. En faisant ainsi, vous pourrez poser \n"
                + "plusieurs tours du même type à la suite."
        );

        textTab[4] = new Text("A tout moment en cliquant sur la tour vous pourrez l'améliorer (si vous avez assez d'argent) ou meme la supprimer."
                + "Un message pour vous dire que vous n'avez plus d'argent s'affichera si c'est le cas;"
                + " il faut cliquer dessus pour le faire disparaitre.");

        textTab[5] = new Text("Lors de la pose des murs, vous pouvez faire varier leur orientation \n"
                + "en appuyant sur la touche espace ou en cliquant droit.");

        textTab[6] = new Text("Pour sauvegarder une partie il faut cliquer sur le bouton 'play' ensuite sauvegarder \n"
                + "Et vous pourrez aussi charger une partie dans le menu principal.");

        textTab[7] = new Text("Vous pourrez également créer vos propres niveaux. "
                + "\n En cliquant sur créateur de niveau il faudra placer comme vous voulez sur le plateau les différents éléments du jeu,"
                + " ensuite cliquer sur le bouton 'play' puis sauvegarder.");

        info = textTab[page];
        info.setFont(new Font("Arial", boardView.getHeight() / 45));
        listText2.add(info);

        info.setWrappingWidth(this.getWidth());
        this.getChildren().add(info);

        if (!isFirst) {
            MenuButton btn_prec = new MenuButton("PRECEDENT");
            btn_prec.setOnMousePressed(e -> {
                SoundController.CLICK();
                setHelpConfig(page - 1);
            });
            listButton.add(btn_prec);
            this.getChildren().add(btn_prec);
        }

        if (!isLast) {
            MenuButton btn_suivant = new MenuButton("SUIVANT");
            btn_suivant.setOnMousePressed(e -> {
                SoundController.CLICK();
                setHelpConfig(page + 1);
            });
            listButton.add(btn_suivant);
            this.getChildren().add(btn_suivant);
        }
        MenuButton btn_retour_menu_principal = new MenuButton("MENU PRINCIPAL");
        btn_retour_menu_principal.setOnMousePressed(e -> {
            SoundController.CLICK();
            setMainMenuConfig();
        });
        listButton.add(btn_retour_menu_principal);

        this.getChildren().add(btn_retour_menu_principal);
        reorganize();
    }

    /**
     * Charge la configuration du menu pause en jeu
     *
     * @param gameView
     * @param isForStageCreator
     * @param isAStage
     */
    public final void setMenuPause(GameView gameView, boolean isForStageCreator, boolean isAStage) {
        clear();

        MenuButton btnResume = new MenuButton("CONTINUER");
        btnResume.setOnMouseClicked(event -> {
            SoundController.CLICK();
            gameView.resume();
        });
        listButton.add(btnResume);
        getChildren().add(btnResume);

        MenuButton btnretour = new MenuButton("RETOUR AU MENU");
        btnretour.setOnMouseClicked(event -> {
            SoundController.CLICK();
            main.backToMenu();
        });
        listButton.add(btnretour);
        getChildren().add(btnretour);

        if (!isAStage) {
            MenuButton btnSave = new MenuButton("SAUVEGARDER");
            btnSave.setOnMouseClicked(event -> {
                SoundController.CLICK();
                if (!isForStageCreator) {
                    gameView.save();
                    boardView.showMessage("La partie a été sauvegardée");

                } else {
                    main.saveStage();
                    boardView.showMessage("Le niveau a été sauvegardé");
                }

            });
            listButton.add(btnSave);
            getChildren().add(btnSave);
        }

        MenuButton btnSounds = new MenuButton(SoundController.getText());
        btnSounds.setOnMouseClicked(event -> {
            SoundController.CLICK();
            SoundController.toggleMute();
            btnSounds.setText(SoundController.getText());
        });
        listButton.add(btnSounds);
        getChildren().add(btnSounds);

        MenuButton btnExit = new MenuButton("QUITTER");
        btnExit.setOnMouseClicked(event -> {
            SoundController.CLICK();
            System.exit(0);
        });
        listButton.add(btnExit);
        getChildren().add(btnExit);

        resizeButton();
        relocateMenuPause();
    }

    public void clear() {
        this.getChildren().clear();
        listButton.clear();
        listSliders.clear();
        listText.clear();
        listText2.clear();
    }

    /*--------------------------------------------------------------------------
    -----------------------------RESIZE-----------------------------------------
    --------------------------------------------------------------------------*/
    public void reorganize() {
        resizeButton();
        main.getMainMenu().layout();
        relocate();
    }

    /**
     * Replace et redimensionne les éléments du menu
     */
    public void relocate() {
        this.setTranslateY(boardView.getTranslateY()
                + boardView.getHeight() / 2.4);
        this.setTranslateX(boardView.getTranslateX()
                + boardView.getWidth() / 2 - this.getWidth() / 2);
        this.setSpacing(boardView.getHeight() / 200);
    }

    public void relocateMenuPause() {
        this.setTranslateY(boardView.getTranslateY()
                + boardView.getHeight() / 2.3);
        this.setTranslateX(0);
        this.setSpacing(boardView.getHeight() / 200);
    }

    /**
     * Redimensionne les boutons
     */
    public void resizeButton() {
        for (MenuButton mb : listButton) {
            mb.redimension(
                    boardView.getWidth() / 7,
                    boardView.getHeight() / 18);
        }
        for (MenuSlider ms : listSliders) {
            ms.redimension(boardView.getWidth() / 7,
                    boardView.getHeight() / 18);
        }
        for (MenuText mt : listText) {
            mt.redimension(boardView.getWidth() / 7,
                    boardView.getHeight() / 42);
        }

        for (Text text : listText2) {
            text.setFont(new Font("Arial", boardView.getHeight() / 42));
            text.setWrappingWidth(boardView.getWidth() / 7);
        }
        layout();
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public BoardView getBoardView() {
        return boardView;
    }
}
