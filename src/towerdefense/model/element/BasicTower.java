package towerdefense.model.element;

import towerdefense.model.bullet.BulletSniper;
import basicobject.Dimension;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;

public class BasicTower extends CannonTower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 203L;

    private static final Dimension DIMENSION = new Dimension(1, 1);
    private static final int RANGE_AT_START = 6;
    private static final int ATTACK_SPEED_AT_START = 100;
    private static final int DAMAGE_AT_START = 5;
    private static final int COST = 75;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BasicTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.CORNFLOWERBLUE);
    }

    public BasicTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.CORNFLOWERBLUE);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    protected void addBullets() {
        addBullet(new BulletSniper(range * Block.getSize(), getCenterPosition(),
                direction_shoot, board.getBoardView(), this));
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public String getElementName() {
        return "Tour basique";
    }

    @Override
    public double getSpeed() {
        return BulletSniper.getSpeed();
    }

}
