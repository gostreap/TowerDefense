/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.element;

import basicobject.Dimension;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.Player;
import towerdefense.model.attacker.Attacker;
import towerdefense.view.ElementView;

/**
 *
 * @author omar
 */
public class Castle extends Element implements java.io.Serializable {

    //private static final long serialVersionUID = 208L;
    private static final Dimension DIMENSION = new Dimension(2, 2);

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Castle(Block block, Board board) {
        super(DIMENSION, block, board, 0, Color.RED);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.RED, board.getPlayer(), this);
    }

    public Castle(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, 0, Color.RED);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.RED, board.getPlayer(), this);
    }

    @Override
    public void effect(LinkedList<Attacker> attackers) {

    }

    @Override
    public String getElementName() {
        return "Château";
    }

    @Override
    public void resetElementView() {
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.PALEVIOLETRED, board.getPlayer(), this);
    }

    public void addToBoard() {
        Player player = board.getPlayer();
        player.add(block);
    }

}
