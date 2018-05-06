package towerdefense.model.element;

import towerdefense.model.bullet.BulletSniper;
import basicobject.Dimension;
import basicobject.Point;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;

public class MultiTargetTower extends CannonTower implements Upgradable,
        java.io.Serializable {

    private static final long serialVersionUID = 206L;

    private static final Dimension DIMENSION = new Dimension(2, 2);
    private static final int RANGE_AT_START = 5;
    private static final int ATTACK_SPEED_AT_START = 200;       //en miliseconde
    private static final int DAMAGE_AT_START = 12;
    private static final int COST = 200;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public MultiTargetTower(Block block, Board board) {
        super(DIMENSION, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.DARKORANGE);
    }

    public MultiTargetTower(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, RANGE_AT_START, ATTACK_SPEED_AT_START,
                DAMAGE_AT_START, COST, Color.DARKORANGE);
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public void addBullets() {
        for (int i = -4; i < 5; i++) {
            double angle = Math.PI / 30 * i;
            double ca = Math.cos(angle);
            double sa = Math.sin(angle);
            double X = direction_shoot.getX();
            double Y = direction_shoot.getY();
            Point direction = new Point(ca * X - sa * Y, sa * X + ca * Y);
            direction = direction.multiply(1 / direction.magnitude());
            addBullet(new BulletSniper(range * Block.getSize(), getCenterPosition(), direction, board.getBoardView(), this));
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public double getSpeed() {
        return BulletSniper.getSpeed();
    }

    public static Dimension getClassDimension() {
        return DIMENSION;
    }

    @Override
    public String getElementName() {
        return "Tour multi-balles";
    }

    public int getDamageUpgrade() {
        return (int) (damage + (double) damage * 2 / 10);
    }

    public int getRangeUpgrade() {
        return range + 1;
    }

    public int getAttackSpeedUpgrade() {
        return attack_speed - 10;
    }

    @Override
    public int getCoefficient() {
        //Il y a plusieurs balle, d'où le coef multiplicateur,
        //par contre 3 c'est au hasard
        return 3 * super.getCoefficient();
    }

}
