package towerdefense.model.element;

import basicobject.Dimension;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.model.attacker.Attacker;

public class BoostTower extends Tower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 205L;

    protected int buff;
    private static final int BUFF_POINT_AT_START = 5;
    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 4;
    private static final int ATTACK_SPEED_AT_START = 250;//en miliseconde
    private static final int COST = 300;

    private int buffLevel;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BoostTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                COST, Color.DARKCYAN);
        buff = BUFF_POINT_AT_START;
    }

    public BoostTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                COST, Color.DARKCYAN);
        buff = BUFF_POINT_AT_START;
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void effect(LinkedList<Attacker> att) {
        boostTower();
    }

    public void boostTower() {
        LinkedList<Tower> adjaTowers = board.getAdjaTower(this);
        for (Tower tower : adjaTowers) {
            if (tower.boost_from_towerboost != buff) {
                tower.boost_from_towerboost = buff;
                tower.getElementView().updateUpgradePane();
            }
        }
    }

    public void removeBoost() {
        LinkedList<Tower> adjaTowers = board.getAdjaTower(this);
        for (Tower tower : adjaTowers) {
            tower.boost_from_towerboost = 0;
            tower.getElementView().updateUpgradePane();
        }
    }

    @Override
    protected void addBullets() {
        return;
    }

    /*--------------------------------------------------------------------------
    -----------------------------UPGRADE----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public boolean upgrade(Player p, int stat) {
        if (p.pay(getCostUpgrade(stat))) {
            upStat(stat);
            buffLevel++;
            return true;
        }
        return false;
    }

    @Override
    public int getCostUpgrade(int stat) {
        return (int) Math.pow(2, buffLevel)
                * COST / 2;
    }

    public void upStat(int stat) {
        switch (stat) {
            case 0:
                this.buff = getBuffUpgrade();
                break;
        }
    }

    @Override
    public LinkedList<String[]> getStatList() {
        LinkedList<String[]> stats_list = new LinkedList<>();
        String[] buffStrings = {"Bonus dégâts", Integer.toString(buff),
            Integer.toString(getBuffUpgrade()),
            Integer.toString(getCostUpgrade(0)) + "$"
        };

        stats_list.add(buffStrings);

        return stats_list;
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public String getElementName() {
        return "Tour boost";
    }

    public int getBuffUpgrade() {
        return (int) (buff + (double) buff * 2 / 10);
    }

    @Override
    public double getSpeed() {
        return -1;
    }
}
