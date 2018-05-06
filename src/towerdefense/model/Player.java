/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model;

import basicobject.Dimension;
import java.util.LinkedList;
import towerdefense.view.CastleView;
import towerdefense.view.RightPanel;

/**
 *
 * @author tristan
 */
public class Player implements java.io.Serializable {

    private static final long serialVersionUID = 6L;

    private Board board;
    private int score;
    private int money;
    private int healthPoint;
    private int maxHealthPoint;
    private LinkedList<Block> originBlocks;
    private transient LinkedList<Block> occupiedBlocks;
    private transient LinkedList<CastleView> castleViews;
    private Dimension dimension;
    private boolean inMenu = false;

    private transient RightPanel rightPanel;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Player(LinkedList<Block> listBlocks, Board board, int hp, int money) {
        score = 0;
        this.money = money;
        healthPoint = hp;
        maxHealthPoint = hp;
        dimension = new Dimension(2, 2);
        originBlocks = listBlocks;
        occupiedBlocks = new LinkedList<>();
        castleViews = new LinkedList<>();
        this.board = board;
        setView();
    }

    public Player(LinkedList<Block> listBlocks, Board board, int hp) {
        this(listBlocks, board, hp, 5000);
    }

    public Player(Board board, int hp, int money) {
        this(new LinkedList<Block>(), board, hp, money);
    }

    /*--------------------------------------------------------------------------
    -------------------------------- METHODS -----------------------------------
    --------------------------------------------------------------------------*/
    /**
     * Ajoute de l'argent et met à jour la vue
     *
     * @param m
     */
    public void addMoney(int m) {
        money += m;
        if (!inMenu) {
            rightPanel.updateMoneyLabel();
        }
    }

    /**
     * Retire de l'argent si possible et met à jour la vue
     *
     * @return True s'il a assez d'argent False Sinon
     * @param cost
     */
    public boolean pay(int cost) {
        if (money >= cost) {
            money -= cost;
            rightPanel.updateMoneyLabel();
            return true;
        } else {
            board.getBoardView().showMessage("Vous n'avez plus assez d'argent!");
            return false;
        }
    }

    /**
     * Rajoute du score et met a jour la vue
     *
     * @param s
     */
    public void increaseScore(int s) {
        if (!inMenu) {
            score += s;
            rightPanel.updateScoreLabel();
        }
    }

    /**
     * Retire de la vie et met à jour la vue
     *
     * @param d
     */
    public void takeDamage(int d) {
        if (!inMenu) {
            healthPoint -= d;
            rightPanel.updateHealthLabel();
            rightPanel.updateLife((double) healthPoint / (double) maxHealthPoint);
        }
    }

    /**
     * @return True si le joueur est mort
     */
    public boolean isDead() {
        if (!inMenu) {
            return healthPoint <= 0;
        } else {
            return false;
        }
    }

    /**
     * Ajoute un chateau à la position du Block
     *
     * @param block
     */
    public void add(Block block) {
        boolean spaceFree = board.isSpaceFree(block, dimension);
        if (spaceFree) {
            originBlocks.add(block);
            setOneView(block);
        }
    }

    /**
     * Ajoute la vue de tous les chateaux qui sont dans la list et met à jours
     * les blocks occupés
     */
    private void setView() {
        for (Block block : originBlocks) {
            setOneView(block);
        }
    }

    /**
     * Ajoute la vue d'un chateau sur le Block block et met a jour les block
     * occupés
     *
     * @param block
     */
    private void setOneView(Block block) {
        for (int i = 0; i < dimension.getHeight(); i++) {
            for (int j = 0; j < dimension.getWidth(); j++) {
                int x = block.X + j;
                int y = block.Y + i;
                board.getBlock(y, x).setOccupied(true);
                occupiedBlocks.add(board.getBlock(y, x));
            }
        }
        CastleView castleView = new CastleView(block.getPosition(),
                dimension, Block.getDimension(), board.getBoardView());
        castleView.addToBoard();
        castleViews.add(castleView);
    }

    public void load() {
        occupiedBlocks = new LinkedList<>();
        castleViews = new LinkedList<>();
        setView();
    }

    public void setInMenu(boolean inMenu) {
        this.inMenu = inMenu;
    }

    public void setRightPanel(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public void resetCastleView() {
        for (CastleView castleView : castleViews) {
            castleView.removeOfBoard();
        }
        setView();
    }

    /*--------------------------------------------------------------------------
    --------------------------------- GETTERS ----------------------------------
    --------------------------------------------------------------------------*/
    public int getScore() {
        return score;
    }

    /**
     * @return Tous les Block qui sont occupés par un chateau
     */
    public LinkedList<Block> getOccupiedBlocks() {
        return occupiedBlocks;
    }

    /**
     * @return Block des position des chateau (Le block en haut a droite)
     */
    public LinkedList<Block> getOriginBlocks() {
        return originBlocks;
    }

    public String healthToString() {
        return healthPoint + " / " + maxHealthPoint;
    }

    public String scoreToString() {
        return Integer.toString(score);
    }

    public String moneyToString() {
        return "$" + Integer.toString(money);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getMoney() {
        return money;
    }

    public int getHealthPoint() {
        return healthPoint;
    }
}
