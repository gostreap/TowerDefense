/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model;

import basicobject.Dimension;
import basicobject.Point;
import java.util.LinkedList;
import towerdefense.model.attacker.Attacker;

/**
 *
 * @author tristan
 */
public class Block implements Comparable<Block>, java.io.Serializable {

    private static final long serialVersionUID = 4L;

    private transient Block[] neighborsBlocks;
    private boolean traversable;
    private boolean occupied;
    private int coefficient;
    private int flyingCoefficient;
    private static double size = 30;
    private static Dimension blockDimension = new Dimension(30, 30);
    public final int X;
    public final int Y;
    private transient LinkedList<Attacker> attackers = new LinkedList<>();

    protected int distance = -1; // sert uniquement pour le calcul du chemin le plus court

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Block(int x, int y) {
        traversable = true;
        occupied = false;
        coefficient = 1;
        flyingCoefficient = 1;
        X = x;
        Y = y;
    }

    /*--------------------------------------------------------------------------
    -----------------------------COMPARABLE-------------------------------------
    --------------------------------------------------------------------------*/
    //Ne sert que dans PathFinder
    @Override
    public int compareTo(Block b) {
        if (distance < b.distance || (b.distance == -1 && distance != -1)) {
            return -1;
        } else if (distance > b.distance) {
            return 1;
        }
        return 0;
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public boolean isTraversable() {
        return traversable;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public int getFlyingCoefficient() {
        return flyingCoefficient;
    }

    public void increaseCoefficient(int n) {
        coefficient += n;
    }

    public void increaseFlyingCoefficient(int n) {
        flyingCoefficient += n;
    }

    public Block[] getNeighborsBlocks() {
        return neighborsBlocks;
    }

    public void setNeighborsBlocks(Block[] neighborsBlocks) {
        this.neighborsBlocks = neighborsBlocks;
    }

    public void setTraversable(boolean b) {
        traversable = b;
    }

    public void setOccupied(boolean b) {
        occupied = b;
    }

    public Point getPosition() {
        return new Point(X * blockDimension.getWidth(), Y * blockDimension.getHeight());
    }

    public Point getCenterPosition() {
        return new Point(X * blockDimension.getWidth() + blockDimension.getWidth() / 2,
                Y * blockDimension.getHeight() + blockDimension.getHeight() / 2);
    }

    public static Dimension getDimension() {
        return blockDimension;
    }

    @Override
    public String toString() {
        return "Block X - " + X + " Y - " + Y;
    }

    public void resetAttackers() {
        attackers = new LinkedList<>();
    }

    public LinkedList<Attacker> getAttackers() {
        return attackers;
    }

    public void addAttacker(Attacker attacker) {
        attackers.add(attacker);
    }

    public void removeAttacker(Attacker attacker) {
        attackers.remove(attacker);
    }

    public int distanceToBlock(Block block) {
        return Math.abs(X - block.X) + Math.abs(Y - block.Y);
    }

    public static double getSize() {
        return size;
    }

    public static void setSize(double size) {
        Block.size = size;
        blockDimension = new Dimension(size, size);
    }

}
