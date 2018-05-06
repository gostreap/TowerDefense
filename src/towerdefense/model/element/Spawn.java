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
import towerdefense.model.attacker.Attacker;
import towerdefense.view.ElementView;

/**
 *
 * @author omarpublic boolean canSpawn() { | public boolean peutJouer(){ return
 * true; | boolean canShoot = lastSpawnTime == -1 | System.curre } |
 * if(canShoot){ | lastSpawnTime = System.currentTimeMillis(); public Point
 * getPosition() { | return true; return null; | } }
 */
public class Spawn extends Element implements java.io.Serializable {

    private static final long serialVersionUID = 208L;

    private static final Dimension DIMENSION = new Dimension(2, 2);
    private double lastSpawnTime = -1;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Spawn(Block block, Board board) {
        super(DIMENSION, block, board, 0, Color.DIMGRAY);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.DIMGRAY, board.getPlayer(), this);
    }

    public Spawn(Block block, Board board, Dimension dimension) {
        super(dimension, block, board, 0, Color.DIMGRAY);
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.DIMGRAY, board.getPlayer(), this);
    }

    @Override
    public void effect(LinkedList<Attacker> attackers) {

    }

    public boolean canSpawn() {
        boolean canShoot = lastSpawnTime == -1 | System.currentTimeMillis() - lastSpawnTime > 20 * board.getTimeFrame();
        if (canShoot) {
            lastSpawnTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public String getElementName() {
        return "Spawn";
    }

    @Override
    public void resetElementView() {
        elementView = new ElementView(block.getPosition(), dimension, Block.getDimension(), board.getBoardView(), Color.DIMGRAY, board.getPlayer(), this);
    }

}
