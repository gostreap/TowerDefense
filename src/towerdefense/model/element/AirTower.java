package towerdefense.model.element;

import towerdefense.model.bullet.BulletSniper;
import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;

public class AirTower extends CannonTower implements Upgradable, java.io.Serializable {

    private static final long serialVersionUID = 202L;

    private static final Dimension DIMENSION = new Dimension(3, 3);
    private static final int RANGE_AT_START = 16;
    private static final int ATTACK_SPEED_AT_START = 50;
    private static final int DAMAGE_AT_START = 30;
    private static final int COST = 700;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public AirTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START, DAMAGE_AT_START, true, COST, Color.DEEPSKYBLUE);
    }

    public AirTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START, DAMAGE_AT_START, true, COST, Color.DEEPSKYBLUE);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    protected void addBullets() {
        double A = direction_shoot.getX();
        double B = direction_shoot.getY();
        Point normal = new Point(-B, A);
        Point depart1 = getCenterPosition().add(normal.multiply(Block.getSize() / 3));
        Point depart2 = getCenterPosition().add(normal.multiply(-Block.getSize() / 3));
        addBullet(new BulletSniper(range * Block.getSize(), depart1, direction_shoot, board.getBoardView(), this));
        addBullet(new BulletSniper(range * Block.getSize(), depart2, direction_shoot, board.getBoardView(), this));

    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public double getSpeed() {
        return BulletSniper.getSpeed();
    }

    @Override
    public String getElementName() {
        return "Tour anti-aérienne";
    }

}
