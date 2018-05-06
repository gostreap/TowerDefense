package towerdefense.model.element;

import basicobject.Dimension;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.model.attacker.Attacker;
import towerdefense.view.ElementView;

public class SpeedReducer extends Element implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 209L;

    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 5;
    private static final int COST = 200;
    private double slow_percent = (double) 30 / 100;
    private double last_slow = System.currentTimeMillis();

    private int[] upgradeLevels = {1, 1};

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public SpeedReducer(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, COST, Color.CYAN);
        elementView = new ElementView(block.getPosition(), dimension,
                Block.getDimension(), board.getBoardView(), Color.CYAN,
                board.getPlayer(), this);
    }

    public SpeedReducer(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, COST, Color.CYAN);
        elementView = new ElementView(block.getPosition(), dimension,
                Block.getDimension(), board.getBoardView(), Color.CYAN,
                board.getPlayer(), this);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void effect(LinkedList<Attacker> attackers) {
        boolean canSlow = System.currentTimeMillis() - last_slow > 40 * board.getTimeCoef();
        if (canSlow) {
            board.removeCoefInRange(this);
            last_slow = System.currentTimeMillis();
            detectAndReduceSpeed(attackers);
            board.addCoefInRange(this);
        }
    }

    public void detectAndReduceSpeed(LinkedList<Attacker> attackers) {
        for (Attacker attacker : attackers) {
            double maxSpeed = attacker.getMaxSpeed();
            double actualSpeed = attacker.getSpeed();
            double slow = slow_percent * maxSpeed / 4;

            if (isInRange(attacker)) {
                if ((double) actualSpeed / maxSpeed > 1 - slow_percent) {
                    attacker.reduceSpeed(slow, 1 - slow_percent);
                }
            } else {
                attacker.increaseSpeed(slow);
            }
        }
    }

    public void resetElementView() {
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.CYAN, board.getPlayer(), this);
    }

    /*--------------------------------------------------------------------------
    -----------------------------UPGRADE----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public boolean upgrade(Player p, int stat) {
        boolean valid = true;
        if (stat == 0 && upgradeLevels[stat] > 10
                || stat == 1 && upgradeLevels[stat] > 5) {
            valid = false;
        }
        if (valid && p.pay(getCostUpgrade(stat))) {
            upStat(stat);
            upgradeLevels[stat]++;
            return true;
        }
        return false;
    }

    public void upStat(int stat) {
        switch (stat) {
            case 0:
                this.slow_percent = getSlowUpgrade();
                break;
            case 1:
                this.range = getRangeUpgrade();
                break;
        }
    }

    @Override
    public LinkedList<String[]> getStatList() {
        LinkedList<String[]> stats_list = new LinkedList<>();
        String[] slow = {"Pourcentage de ralentissement", Integer.toString((int) (slow_percent * 100)),
            Integer.toString((int) (getSlowUpgrade() * 100)),
            Integer.toString(getCostUpgrade(0)) + "$"
        };
        String[] range = {"Portée", Integer.toString(this.range),
            Integer.toString(getRangeUpgrade()),
            Integer.toString(getCostUpgrade(1)) + "$"
        };

        stats_list.add(slow);
        stats_list.add(range);

        return stats_list;
    }

    public double getSlowUpgrade() {
        return slow_percent + 0.05;
    }

    public int getRangeUpgrade() {
        return range + 1;
    }

    @Override
    public int getCostUpgrade(int stat) {
        return (int) Math.pow(2, upgradeLevels[stat] - 1)
                * COST / 2;
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public String getElementName() {
        return "Reducteur de vitesse";
    }
}
