package towerdefense.model.element;

import towerdefense.model.bullet.BulletSniper;
import basicobject.Dimension;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;

public class SniperTower extends CannonTower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 207L;

    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 20;
    private static final int ATTACK_SPEED_AT_START = 400;       //en miliseconde
    private static final int DAMAGE_AT_START = 60;
    private static final int COST = 400;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public SniperTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.GREEN);
    }

    public SniperTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.GREEN);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    protected void addBullets() {
        addBullet(new BulletSniper(range * Block.getSize(), getCenterPosition(), direction_shoot, board.getBoardView(), this));
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public Color getColor() {
        return Color.BURLYWOOD;
    }

    @Override
    public String getElementName() {
        return "Tour Sniper";
    }

    @Override
    public double getSpeed() {
        return BulletSniper.getSpeed();
    }
}
