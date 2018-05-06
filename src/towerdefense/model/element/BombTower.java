package towerdefense.model.element;

import towerdefense.model.bullet.BulletBomb;
import basicobject.Dimension;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;

public class BombTower extends CannonTower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 204L;

    protected int area_of_effect;
    private static final int AERA_OF_EFFECT_AT_START = 1;
    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 5;
    private static final int ATTACK_SPEED_AT_START = 300;       //en miliseconde
    private static final int DAMAGE_AT_START = 12;
    private static final int COST = 300;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BombTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.CRIMSON);
        area_of_effect = AERA_OF_EFFECT_AT_START;
    }

    public BombTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.CRIMSON);
        area_of_effect = AERA_OF_EFFECT_AT_START;
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    protected void addBullets() {
        addBullet(new BulletBomb(range * Block.getSize(), getCenterPosition(),
                direction_shoot, board.getBoardView(), this));
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public double getSpeed() {
        return BulletBomb.getSpeed();
    }

    @Override
    public String getElementName() {
        return "Tour bombe";
    }

}
