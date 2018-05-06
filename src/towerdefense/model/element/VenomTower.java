package towerdefense.model.element;

import towerdefense.model.bullet.BulletSniper;
import basicobject.Dimension;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.bullet.BulletVenom;

public class VenomTower extends CannonTower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 207L;

    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 5;
    private static final int ATTACK_SPEED_AT_START = 400;
    private static final int DAMAGE_AT_START = 3;
    private static final int COST = 200;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public VenomTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.web("2dd62a"));
    }

    public VenomTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.web("2dd62a"));
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    protected void addBullets() {
        addBullet(new BulletVenom(range * Block.getSize(), getCenterPosition(),
                direction_shoot, board.getBoardView(), this));
    }

    @Override
    public int damageUpgrade() {
        return (int) (damage + 1);
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public Color getColor() {
        return Color.web("2dd62a");
    }

    @Override
    public String getElementName() {
        return "Tour poison";
    }

    @Override
    public double getSpeed() {
        return BulletSniper.getSpeed();
    }
}
