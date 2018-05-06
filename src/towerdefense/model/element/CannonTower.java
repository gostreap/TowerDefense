package towerdefense.model.element;

import basicobject.Dimension;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;

public abstract class CannonTower extends Tower implements Upgradable {

    private int[] upgradeLevels = {1, 1, 1};

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public CannonTower(Dimension dimension, Block block, Board board, int range,
            int attack_speed, int cost, Color color) {
        this(dimension, block, board, range, attack_speed, 0,
                false, cost, color);
    }

    public CannonTower(Dimension dimension, Block block, Board board, int range,
            int attack_speed, int damage, int cost, Color color) {
        this(dimension, block, board, range, attack_speed, damage,
                false, cost, color);
    }

    public CannonTower(Dimension dimension, Block block, Board board,
            int range, int attack_speed, int damage, boolean flying_unit,
            int cost, Color color) {
        super(dimension, block, board, range, attack_speed, damage,
                flying_unit, cost, color);
    }

    /*--------------------------------------------------------------------------
    -----------------------------UPGRADE----------------------------------------
    --------------------------------------------------------------------------*/
    public boolean upgrade(Player p, int stat) {
        boolean valid = true;
        if (stat == 0 && upgradeLevels[stat] >= 20
                || stat == 1 && upgradeLevels[stat] >= 8
                || stat == 2 && upgradeLevels[stat] >= 8) {
            valid = false;
        }
        if (valid && p.pay(getCostUpgrade(stat))) {
            board.removeCoefInRange(this);
            upStat(stat);
            upgradeLevels[stat]++;
            board.addCoefInRange(this);
            return true;
        }
        return false;
    }

    public void upStat(int stat) {
        switch (stat) {
            case 0:
                this.damage = damageUpgrade();
                break;
            case 1:
                this.range = rangeUpgrade();
                break;
            case 2:
                this.attack_speed = attackSpeedUpgrade();
                break;
        }
    }

    public int getCostUpgrade(int stat) {
        return (int) Math.pow(2, upgradeLevels[stat] - 1)
                * COST / 2;
    }

    public int damageUpgrade() {
        return (int) (damage + (double) damage * 2 / 10);
    }

    public int rangeUpgrade() {
        return range + 1;
    }

    public int attackSpeedUpgrade() {
        return attack_speed - 10;
    }

    @Override
    public LinkedList<String[]> getStatList() {
        LinkedList<String[]> stats_list = new LinkedList<>();
        String[] damageTab = {"Dégâts", Integer.toString(damage),
            Integer.toString(damageUpgrade()), Integer.toString(getCostUpgrade(0)) + "$"
        };
        String[] range = {"Portée", Integer.toString(this.range),
            Integer.toString(rangeUpgrade()), Integer.toString(getCostUpgrade(1)) + "$"
        };
        String[] attack_speed = {"Temps entre chaque tir",
            Integer.toString(this.attack_speed),
            Integer.toString(attackSpeedUpgrade()), Integer.toString(getCostUpgrade(2)) + "$"
        };

        stats_list.add(damageTab);
        stats_list.add(range);
        stats_list.add(attack_speed);

        return stats_list;
    }
}
