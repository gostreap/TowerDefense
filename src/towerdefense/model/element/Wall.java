/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.element;

/**
 *
 * @author omar
 */
import basicobject.Dimension;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.attacker.Attacker;
import towerdefense.view.ElementView;

public class Wall extends Element implements java.io.Serializable {

    private static final long serialVersionUID = 210L;

    private static final int COST = 50;
    private static final Dimension DIMENSION = new Dimension(4, 2);

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Wall(Block block, Board board) {
        super(DIMENSION, block, board, COST, Color.LIGHTSLATEGRAY);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.LIGHTSLATEGREY, board.getPlayer(), this);
    }

    public Wall(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, COST, Color.LIGHTSLATEGRAY);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.LIGHTSLATEGREY, board.getPlayer(), this);
    }

    @Override
    public void effect(LinkedList<Attacker> attackers) {
    }

    @Override
    public String getElementName() {
        return "Wall";
    }

    @Override
    public void resetElementView() {
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.LIGHTSLATEGREY, board.getPlayer(), this);
    }
}
