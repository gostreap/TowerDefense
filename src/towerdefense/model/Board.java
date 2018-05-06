/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model;

import basicobject.Dimension;
import basicobject.ElementPosition;
import basicobject.ListElementPosition;
import basicobject.Point;
import basicobject.StageSave;
import java.util.LinkedList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import towerdefense.controller.Main;
import towerdefense.model.attacker.*;
import towerdefense.model.element.*;
import towerdefense.view.BoardView;
import towerdefense.view.ElementView;
import towerdefense.view.RightPanel;

/**
 *
 * @author tristan
 */
public class Board implements java.io.Serializable {

    private static final long serialVersionUID = 5L;

    private transient BoardView boardView;
    private transient Main main;

    private Player player;
    private LinkedList<Element> listElement = new LinkedList<>();
    private LinkedList<Attacker> listAttackerOnBoard = new LinkedList<>();
    private LinkedList<Attacker> listWaveAttackers = new LinkedList<>();
    private LinkedList<Attacker> attackerNeedRemove = new LinkedList<>();
    private LinkedList<Spawn> tabSpawn = new LinkedList<>();
    private Block[][] tabBlocks;

    private transient PathFinder pathFinder;

    private transient Timeline waveTimeline;
    private int wave;
    private boolean isOnPause;
    private final int timeUnit = 8;
    private int timeCoef = 4;

    public final int width;
    public final int height;

    private double sizeBlock;
    private boolean isForStageCreator;
    private boolean isStarted;

    private int stageLevel = -1;

//    public transient SoundController soundController = new SoundController();

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Board(int width, int height, double windowWidth, double windowHeight,
            Main main, int hp, int nbSpawn, int nbCastle) {

        this(width, height, windowWidth, windowHeight, main, hp);

        randomSpawnAndPlayer(nbSpawn, nbCastle, hp);
    }

    public Board(int width, int height, double windowWidth, double windowHeight,
            Main main, int hp, boolean isForStageCreator, int money) {

        this(width, height, windowWidth, windowHeight, main, hp);

        this.isForStageCreator = isForStageCreator;

        player = new Player(this, hp, money);
        waveTimeline = new Timeline();
    }

    public Board(int width, int height, double windowWidth, double windowHeight,
            Main main, int hp, ListElementPosition lep, int money) {

        this(width, height, windowWidth, windowHeight, main, hp);

        player = new Player(this, hp, money);

        initBoard(lep);
    }

    public Board(int width, int height, double windowWidth, double windowHeight,
            Main main, int hp, ListElementPosition lep, int money, int stageLevel) {

        this(width, height, windowWidth, windowHeight, main, hp, lep, money);
        this.stageLevel = stageLevel;

    }

    public Board(int width, int height, double windowWidth, double windowHeight,
            Main main, int hp) {
        this.width = width;
        this.height = height;

        double size = Double.min((windowWidth - 195) / width, windowHeight / height);
        Block.setSize(size);

        this.boardView = new BoardView(this);

        this.main = main;

        initTabBlocks(width, height);

        this.pathFinder = new PathFinder(this);
    }

    /**
     * Place aléatoirement nbSpawn spawn.s, nbCastle catle.s et cré un joueur
     * avec hp vie.s
     *
     * @param nbSpawn
     * @param nbCastle
     * @param hp
     */
    public void randomSpawnAndPlayer(int nbSpawn, int nbCastle, int hp) {
        Random rand = new Random();

        Block[] spawnBlocks = new Block[nbSpawn];
        LinkedList<Block> playerBlocks = new LinkedList<>();

        boolean validConfig = false;

        while (!validConfig) {
            int comptor = 0;
            boolean validSpawn = true;
            do {
                comptor++;
                for (int i = 0; i < nbSpawn; i++) {
                    spawnBlocks[i] = tabBlocks[rand.nextInt(height - 1)][rand.nextInt(height - 1)];
                    for (int j = 0; j < i; j++) {
                        int distance = spawnBlocks[j].distanceToBlock(spawnBlocks[i]);
                        if (distance < height / 5 + width / 5) {
                            validSpawn = false;
                        }
                    }
                }
            } while (!validSpawn && comptor < 20);

            if (!validSpawn) {
                continue;
            }

            comptor = 0;
            do {
                comptor++;
                playerBlocks.clear();
                for (int i = 0; i < nbCastle; i++) {
                    playerBlocks.add(tabBlocks[rand.nextInt(height - 1)][rand.nextInt(height - 1)]);
                    for (int j = 0; j < nbSpawn; j++) {
                        int distance = spawnBlocks[j].distanceToBlock(playerBlocks.get(i));
                        if (distance < height / 4 + width / 4) {
                            validSpawn = false;
                        }
                    }
                    for (int j = 0; j < i; j++) {
                        int distance = playerBlocks.get(j).distanceToBlock(playerBlocks.get(i));
                        if (distance < height / 10 + width / 10) {
                            validSpawn = false;
                        }
                    }
                }
            } while (!validSpawn && comptor < 20);

            validConfig = validSpawn;
        }
        for (int i = 0; i < nbSpawn; i++) {
            addSpawn(new Spawn(spawnBlocks[i], this));
        }
        player = new Player(playerBlocks, this, hp);
    }

    // initialize the array of blocks and set the neighbors
    private void initTabBlocks(int width, int height) {
        tabBlocks = new Block[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tabBlocks[i][j] = new Block(j, i);
            }
        }
        resetBlockAttributes();
    }

    /*--------------------------------------------------------------------------
    -----------------------------VAGUES-----------------------------------------
    --------------------------------------------------------------------------*/
    public void play() {
        wave = 1;
        playWave();
    }

    public void playWave() {
        initAttackers();
        waveTimeline = new Timeline(new KeyFrame(
                Duration.millis(timeUnit * timeCoef),
                ae -> {
                    if (isStarted) {
                        playFrame();
                    }
                }));
        waveTimeline.setCycleCount(Timeline.INDEFINITE);
        waveTimeline.play();
    }

    public void playFrame() {
        RightPanel rightPanel = player.getRightPanel();
        if (rightPanel != null) {
            rightPanel.updateTimeFromBegining();
            rightPanel.setImagePause();
        }

        for (int i = 0; i < tabSpawn.size(); i++) {
            if (tabSpawn.get(i).canSpawn()) {
                if (!(listWaveAttackers.isEmpty())) {
                    Attacker att = listWaveAttackers.pollFirst();
                    att.setPosition(tabSpawn.get(i).getCenterPosition());
                    att.resetRoad();
                    att.getAttackerView().addToBoard();
                    listAttackerOnBoard.add(att);
                }
            }
        }
        actionAttackers();
        actionTowers();
        if ((listWaveAttackers.isEmpty() && listAttackerOnBoard.isEmpty())
                || player.isDead()) {
            waveTimeline.stop();
            if (!player.isDead()) {
                wave++;
                playWave();
            } else {
                onFinished();
            }
        }
    }

    private void initAttackers() {
        Random rand = new Random();
        int nbAttacker = (int) (10 + (0.6 + rand.nextInt(10) / 40)
                * wave * Math.sqrt(wave) / 2);

        listWaveAttackers = new LinkedList<>();

        int basic = 0;
        int fast = 0;
        int patapouf = 0;
        int healer = 0;
        int flying = 0;
        int typeOfWave = rand.nextInt(100);
        if (typeOfWave < 30) { //Basic Wave
            basic = 90;
            healer = 10 + basic;
        } else if (typeOfWave < 50) { //Fast Wave
            fast = 100;
        } else if (typeOfWave < 70) { //Patapouf Wave
            patapouf = 100;
        } else if (typeOfWave < 90) { //Flying Wave
            flying = 100;
        } else if (typeOfWave < 100) { //Mixt Wave
            basic = 50;
            fast = 25 + basic;
            patapouf = 10 + fast;
            healer = 5 + patapouf;
            flying = 10 + healer;
        }

        for (int i = 0; i < nbAttacker; i++) {
            int type = rand.nextInt(100) + 1;
            if (type <= basic) {
                listWaveAttackers.add(new Basic(Point.ZERO, this, wave));
            } else if (type <= fast) {
                listWaveAttackers.add(new Fast(Point.ZERO, this, wave));
            } else if (type <= patapouf) {
                listWaveAttackers.add(new Patapouf(Point.ZERO, this, wave));
            } else if (type <= healer) {
                listWaveAttackers.add(new Healer(Point.ZERO, this, wave));
            } else if (type <= flying) {
                listWaveAttackers.add(new FlyingUnit(Point.ZERO, this, wave));
            }
        }
    }

    public void actionTowers() {
        for (Element element : listElement) {
            element.effect(listAttackerOnBoard);
        }
    }

    public void actionAttackers() {
        for (Attacker attacker : listAttackerOnBoard) {
            attacker.effect(listAttackerOnBoard);
        }
        for (Attacker attacker : attackerNeedRemove) {
            attacker.getAttackerView().removeText();
            removeAttacker(attacker);
        }
        attackerNeedRemove.clear();
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODES---------------------------------------
    --------------------------------------------------------------------------*/
    // Renvoie faux si un des attaquants ne peux pas changer de route
    // (si le chemin est bloqué)
    public boolean resetAttackerRoad() {
        for (Attacker attacker : listAttackerOnBoard) {
            if (!attacker.resetRoad()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAndResetAttackerRoad() {
        if (!isPathAvailableFromSpawns()) {
            return false;
        }
        return resetAttackerRoad();
    }

    public void addToRemove(Attacker attacker) {
        attackerNeedRemove.add(attacker);
    }

    public void removeAttacker(Attacker attacker) {
        listAttackerOnBoard.remove(attacker);
    }

    public void removeListeners() {
        for (Element el : listElement) {
            ElementView elementView = el.getElementView();
            elementView.removeListener();
        }
    }

    public LinkedList<Point> findPath(Attacker attacker) {
        return pathFinder.findPath(attacker);
    }

    public void addElement(Element e) {
        if (isOnPause) {
            return;
        }
        if (player.pay(e.COST)) {
            if (!addElementWithoutPay(e)) {
                player.addMoney(e.COST);
            }
        }
    }

    public boolean addElementWithoutPay(Element e) {
        boolean isPlaced = true;
        if (waveTimeline != null) {
            waveTimeline.pause();
        }

        Block elemBlock = e.getBlock();
        Dimension dim = e.getDimension();

        // On verifie qu'il n'y a rien de présent sur les cases demandés
        boolean spaceFree = isSpaceFree(elemBlock, dim);

        //Si c'est le cas, on vérifie que cela ne bloque pas de chemins et si
        //c'est le cas, on met à jour les chemins
        if (spaceFree) {
            listElement.add(e);
            changeBlockPropertyElement(e, true);
            addCoefInRange(e);
            if (!checkAndResetAttackerRoad() && !isForStageCreator) {
                removeElement(e, true);
                isPlaced = false;
            } else {
                e.getElementView().addToBoard();
            }
        } else {
            isPlaced = false;
        }

        if (!isOnPause && waveTimeline != null) {
            waveTimeline.play();
        }
        return isPlaced;
    }

    public void addSpawn(Spawn spawn) {
        Block spawnBlock = spawn.getBlock();
        Dimension dim = spawn.getDimension();

        // On verifie qu'il n'y a rien de présent sur les cases demandés
        boolean spaceFree = isSpaceFree(spawnBlock, dim);

        if (spaceFree) {
            changeOccupiedProperty(spawnBlock, dim, true);
            spawn.getElementView().addToBoard();
            tabSpawn.add(spawn);
        }
    }

    public void removeElement(Element e) {
        removeElement(e, false);
    }

    private void removeElement(Element e, boolean check) {
        if (!check) {
            waveTimeline.pause();
        }
        listElement.remove(e);

        changeBlockPropertyElement(e, false);

        removeCoefInRange(e);

        if (!check) {
            e.getElementView().removeOfBoard();
            resetAttackerRoad();
            if (!isOnPause) {
                waveTimeline.play();
            }
        }

    }

    public void addCoefInRange(Element e) {
        changeCoefInRange(e, e.getCoefficient());
    }

    public void removeCoefInRange(Element e) {
        changeCoefInRange(e, -e.getCoefficient());
    }

    private void changeCoefInRange(Element e, int coef) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Block b = tabBlocks[i][j];
                if (b.getCenterPosition().distance(e.getCenterPosition())
                        < e.getRange() * Block.getSize()) {
                    if (e instanceof AirTower) {
                        b.increaseFlyingCoefficient(coef);
                    } else {
                        b.increaseCoefficient(coef);
                    }
                }
            }
        }
    }

    // temps linéaire en nombre d'attaquants et taille dimension
    public boolean isSpaceFree(Block block, Dimension dim) {
        if (block == null) {
            return false;
        }
        if (block.X + dim.getWidth() > width + 0.05
                || block.Y + dim.getHeight() > height + 0.05) {
            return false;
        }
        for (int i = 0; i < dim.getHeight(); i++) {
            for (int j = 0; j < dim.getWidth(); j++) {
                if (!isBlockFree(tabBlocks[block.Y + i][block.X + j])) {
                    return false;
                }
            }
        }
        return true;
    }

    // temps linéaire en nombre d'attaquants
    private boolean isBlockFree(Block block) {
//		waveTimeline.pause();
        if (block.isOccupied()) {
            return false;
        }
        for (Attacker att : listAttackerOnBoard) {
            if (att.getBlock() == block) {
                return false;
            }
        }
//		waveTimeline.play();
        return true;
    }

    // Renvoie vraie si les isPathAvailableFromSpawn renvoi vrai et
    // Si il existe un chemin vers un chateau depuis tout les attaquants
    public boolean isPathAvailable(Block block, Dimension dim) {
        waveTimeline.pause();
        boolean rep = true;

        changeTraversableProperty(block, dim, false);

        rep = isPathAvailableFromSpawns();

        for (Attacker attacker : listAttackerOnBoard) {
            if (!rep) {
                break;
            }
            LinkedList<Point> road = findPath(attacker);
            if (road == null) {
                rep = false;
            }
        }

        changeTraversableProperty(block, dim, true);

        if (!isOnPause) {
            waveTimeline.play();
        }
        return rep;
    }

    private void changeTraversableProperty(Block block, Dimension dim, boolean b) {
        for (int i = 0; i < dim.getHeight(); i++) {
            for (int j = 0; j < dim.getWidth(); j++) {
                tabBlocks[block.Y + i][block.X + j].setTraversable(b);
            }
        }
    }

    private void changeOccupiedProperty(Block block, Dimension dim, boolean b) {
        for (int i = 0; i < dim.getHeight(); i++) {
            for (int j = 0; j < dim.getWidth(); j++) {
                tabBlocks[block.Y + i][block.X + j].setOccupied(b);
            }
        }
    }

    // Si add vaut vrai, les cases occupées par e sont réglées à occupé et non traversable
    // Si add vaut faux, les cases occupées par e sont réglées à non occupé et traversable
    private void changeBlockPropertyElement(Element e, boolean add) {
        Block elemBlock = e.getBlock();
        Dimension dim = e.getDimension();
        for (int i = 0; i < dim.getHeight(); i++) {
            for (int j = 0; j < dim.getWidth(); j++) {
                int x = elemBlock.X + j;
                int y = elemBlock.Y + i;
                tabBlocks[y][x].setTraversable(!add);
                tabBlocks[y][x].setOccupied(add);
            }
        }
    }

    // Complexité : nombre de spawn * nombre de chateau Djikstra
    private boolean isPathAvailableFromSpawns() {
        LinkedList<Block> castlesBlocks = player.getOriginBlocks();
        Boolean[] tabCastleReached = new Boolean[castlesBlocks.size()];

        //étrange de devoir faire la boucle qui suit mais sinon j'ai un nul pointeur plus bas
        for (int i = 0; i < tabCastleReached.length; i++) {
            tabCastleReached[i] = false;
        }

        /*On vérifie que pour chaque spawn il y a un chemin vers un chateau
        et que chaque chateau est accessible depuis un spawn*/
        for (Spawn spawn : tabSpawn) {
            boolean castleAvailable = false;
            int i = 0;
            for (Block block : castlesBlocks) {
                if (pathFinder.findPath(spawn.getCenterPosition(), block) != null) {
                    castleAvailable = true;
                    tabCastleReached[i] = true;
                }
                i++;
            }
            // Si le spawn ne permet pas d'accéder à un chateau, on renvoie faux
            if (!castleAvailable) {
                return false;
            }
        }

        //Si un chateau n'est pas atteignable, on renvoie faux.
        for (int i = 0; i < tabCastleReached.length; i++) {
            if (!tabCastleReached[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Met en pause si la partie n'est pas en pause et si la partie est en
     * pause, la réactive
     *
     */
    public boolean togglePause() {
        if (isOnPause) {
            waveTimeline.play();
            //BUG:les tours rédemarrent le jeu quand c'est en pause
            isOnPause = false;
            return false;
        } else {
            waveTimeline.pause();
            isOnPause = true;
            return true;
        }
    }

    public void stop() {
        waveTimeline.stop();
    }

    /**
     * Augmente la vitesse de jeu modulo
     */
    public void toggleTimeSpeed() {
        if (!isStarted) {
            return;
        }
        timeCoef = (timeCoef == 1) ? 4 : timeCoef / 2;
        waveTimeline.stop();
        waveTimeline = new Timeline(new KeyFrame(
                Duration.millis(timeUnit * timeCoef),
                ae -> {
                    playFrame();
                }));
        waveTimeline.setCycleCount(Timeline.INDEFINITE);
        if (!isOnPause) {
            waveTimeline.play();
        }
    }

    /*--------------------------------------------------------------------------
    ---------------------------REDIMENSIONNEMENT--------------------------------
    --------------------------------------------------------------------------*/
    /**
     * Redimensionne le plateau, la vue du plateau et tout ce qu'ils contiennent
     *
     * @param width
     * @param height
     */
    public void resize(double width, double height) {
        if (waveTimeline != null) {
            waveTimeline.pause();
        }
        if (width == 0 || height == 0) {
            return;
        }
        double oldSize = Block.getSize();
        sizeBlock = Double.min(width / this.width, height / this.height);
        Block.setSize(sizeBlock);

        double factor = sizeBlock / oldSize;

        for (Attacker attacker : listAttackerOnBoard) {
            attacker.resize(factor);
        }
        if (listWaveAttackers != null) {
            for (Attacker attacker : listWaveAttackers) {
                attacker.resize(factor);
            }
        }

        for (Element element : listElement) {
            element.resize(factor);
        }
//
        for (Spawn spawn : tabSpawn) {
            spawn.resize(factor);
        }

        player.resetCastleView();
        if (waveTimeline != null && !isOnPause) {
            waveTimeline.play();
        }

    }

    public void onFinished() {
        waveTimeline = null;
        if (isAStage()) {
            main.toFinalMenu(stageLevel);
        } else {
            main.toFinalMenu();
        }
    }

    public boolean isAStage() {
        return stageLevel != -1;
    }

    public void start() {
        isStarted = true;
    }

    /*--------------------------------------------------------------------------
    -----------------------------SAUVEGARDES------------------------------------
    --------------------------------------------------------------------------*/
    /**
     * A appeler lors du chargement d'une nouvelle partie. Recharge tout les
     * attributs
     */
    public void save() {
        main.SaveGame("Partie.ser");
    }

    public void load() {
        Block.setSize(sizeBlock);
        this.pathFinder = new PathFinder(this);
//        this.soundController = new SoundController();
        waveTimeline = new Timeline(new KeyFrame(
                Duration.millis(timeUnit * timeCoef),
                ae -> {
                    playFrame();
                }));
        waveTimeline.setCycleCount(Timeline.INDEFINITE);
        resetBlockAttributes();
        resetBoardView();
    }

    /**
     * Remet à zéro la vue du plateau
     */
    public void resetBoardView() {
        boardView = new BoardView(this);

        player.load();
        player.resetCastleView();

        for (Attacker att : listAttackerOnBoard) {
            att.resetAttackerView();
            att.getAttackerView().addToBoard();
            att.getBlock().addAttacker(att);
        }

        for (Attacker att : listWaveAttackers) {
            att.resetAttackerView();
        }

        for (Spawn spawn : tabSpawn) {
            spawn.resetElementView();
            spawn.getElementView().addToBoard();
        }

        for (Element element : listElement) {
            element.resetElementView();
            element.getElementView().addToBoard();
        }
    }

    /**
     * Remet à zéro certains attributs de tout les blocks
     */
    public void resetBlockAttributes() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tabBlocks[i][j].resetAttackers();
                Block[] neighbors = new Block[4];
                if (i > 0) {
                    neighbors[0] = tabBlocks[i - 1][j];
                }
                if (j > 0) {
                    neighbors[1] = tabBlocks[i][j - 1];
                }
                if (j < width - 1) {
                    neighbors[2] = tabBlocks[i][j + 1];
                }
                if (i < height - 1) {
                    neighbors[3] = tabBlocks[i + 1][j];
                }
                tabBlocks[i][j].setNeighborsBlocks(neighbors);
            }
        }
    }

    /*--------------------------------------------------------------------------
    ------------------------CHARGEMENT NIVEAU-----------------------------------
    --------------------------------------------------------------------------*/
    public void initBoard(ListElementPosition listElementPosition) {
        int X, Y;
        Block b;
        Dimension dim;
        for (ElementPosition elementPosition : listElementPosition.getList()) {
            switch (elementPosition.elementName) {
                case "Spawn":
                    X = (int) elementPosition.position.getX();
                    Y = (int) elementPosition.position.getY();
                    b = tabBlocks[Y][X];
                    dim = elementPosition.dimension;
                    addSpawn(new Spawn(b, this, dim));
                    break;
                case "Wall":
                    X = (int) elementPosition.position.getX();
                    Y = (int) elementPosition.position.getY();
                    b = tabBlocks[Y][X];
                    dim = elementPosition.dimension;
                    addElementWithoutPay(new Wall(b, this, dim));
                    break;
                case "Castle":
                    X = (int) elementPosition.position.getX();
                    Y = (int) elementPosition.position.getY();
                    b = tabBlocks[Y][X];
                    dim = elementPosition.dimension;
                    Castle castle = new Castle(b, this, dim);
                    castle.addToBoard();
                    break;
            }
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public ListElementPosition getListElementPosition() {
        ListElementPosition lep = new ListElementPosition();
        for (Spawn spawn : tabSpawn) {
            Block b = spawn.getBlock();
            Point position = new Point(b.X, b.Y);
            ElementPosition elementPosition = new ElementPosition(spawn.getDimension(), position, "Spawn");
            lep.add(elementPosition);
        }

        for (Block b : player.getOriginBlocks()) {
            Point position = new Point(b.X, b.Y);
            ElementPosition elementPosition = new ElementPosition(player.getDimension(), position, "Castle");
            lep.add(elementPosition);
        }

        for (Element element : listElement) {
            if (element instanceof Wall) {
                Block b = element.getBlock();
                Point position = new Point(b.X, b.Y);
                ElementPosition elementPosition = new ElementPosition(element.getDimension(), position, "Wall");
                lep.add(elementPosition);

            }
        }

        return lep;
    }

    public StageSave getStageSave() {
        return new StageSave(getListElementPosition(), player.getHealthPoint(),
                width, height, player.getMoney());
    }

    public Main getMain() {
        return main;
    }

    /**
     *
     * @param tower
     * @return la liste des tours adjacentes à tower
     */
    public LinkedList<Tower> getAdjaTower(Tower tower) {
        LinkedList<Tower> adjaTowers = new LinkedList<>();

        for (Element element : listElement) {
            if (element instanceof Tower) {
                if (tower.getCenterPosition().
                        distance(element.getCenterPosition())
                        < tower.getRange() * Block.getSize()) {
                    adjaTowers.add((Tower) element);
                }
            }
        }
        return adjaTowers;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getTimeCoef() {
        return timeCoef;
    }

    /**
     *
     * @param pos
     * @return le block contenant le point pos
     */
    public Block getBlockOf(Point pos) {
        int colonne = (int) (pos.getX() / Block.getSize());
        int ligne = (int) (pos.getY() / Block.getSize());
        if (colonne >= width || ligne >= height || colonne < 0 || ligne < 0) {
            return null;
        }
        return tabBlocks[ligne][colonne];
    }

    public Block[][] getTabBlocks() {
        return tabBlocks;
    }

    public LinkedList<Block> getPlayerBlocks() {
        return player.getOccupiedBlocks();
    }

    public Player getPlayer() {
        return player;
    }

    public double getPixelHeight() {
        return height * Block.getSize();
    }

    public double getPixelWidth() {
        return width * Block.getSize();
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public Block getBlock(int y, int x) {
        return tabBlocks[y][x];
    }

    public int[] getBlockPlace(Block block) {
        int[] coord = {block.Y, block.X};
        return coord;
    }

    public int getTimeFrame() {
        return timeCoef * timeUnit;
    }

    public boolean isStarted() {
        return isStarted;
    }

}
