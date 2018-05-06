/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model;

import basicobject.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import towerdefense.model.attacker.Attacker;
import towerdefense.model.attacker.FlyingUnit;

/**
 *
 * @author tristan
 */
public class PathFinder {

    private final int height;
    private final int width;
    private final Board board;

    private final Block[][] tabBlocks;
    private final Block[][] tabPredecessors;

    PriorityQueue<Block> notSettled = new PriorityQueue<>();
    HashSet<Block> settled = new HashSet<>();

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public PathFinder(Board board) {
        this.board = board;
        this.tabBlocks = board.getTabBlocks();
        this.height = tabBlocks.length;
        this.width = tabBlocks[0].length;
        this.tabPredecessors = new Block[height][width];
    }

    /*--------------------------------------------------------------------------
    -----------------------------FINDPATHS--------------------------------------
    --------------------------------------------------------------------------*/
    public LinkedList<Point> findPath(Attacker attacker) {
        if (attacker instanceof FlyingUnit) {
            return findPath(attacker.getPosition(), true);
        }
        return findPath(attacker.getPosition(), false);
    }

    public LinkedList<Point> findPath(Point point) {
        return findPath(point, board.getPlayerBlocks(), false);
    }

    public LinkedList<Point> findPath(Point point, Block endBlock) {
        LinkedList<Block> listEnd = new LinkedList<>();
        listEnd.add(endBlock);
        return findPath(point, listEnd, false);
    }

    public LinkedList<Point> findPath(Point point, boolean flying) {
        return findPath(point, board.getPlayerBlocks(), flying);
    }

    public LinkedList<Point> findPath(Point point, LinkedList<Block> listEnd, boolean flying) {
        LinkedList<Block> blocksPath = start(board.getBlockOf(point), listEnd, flying);

        if (blocksPath == null) {
            return null;
        }

        LinkedList<Point> pointsPath = new LinkedList<>();
        for (Block block : blocksPath) {
            pointsPath.add(block.getCenterPosition());
        }
        return pointsPath;
    }

    /*--------------------------------------------------------------------------
    -----------------------------DIJKSTRA---------------------------------------
    --------------------------------------------------------------------------*/
    private void initialisation(LinkedList<Block> listEnd) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tabBlocks[i][j].distance = -1;
            }
        }
        for (Block b : listEnd) {
            b.distance = 0;
            notSettled.add(b);
        }
    }

    private Block find_min() {
        return notSettled.remove();
    }

    private void update_distances(Block b1, Block b2, boolean flying) {
        int newDistance = b1.distance;
        if (flying) {
            newDistance += b2.getFlyingCoefficient();
        } else {
            newDistance += b2.getCoefficient();
        }
        if (b2.distance > newDistance || (b2.distance == -1)) {
            b2.distance = newDistance;
            tabPredecessors[b2.Y][b2.X] = b1;
            notSettled.add(b2);
        }
    }

    private LinkedList<Block> start(Block bstart, LinkedList<Block> listEnd, boolean flying) {
        initialisation(listEnd);

        while (!notSettled.isEmpty()) {
            Block b1 = find_min();
            Block[] neighbors = b1.getNeighborsBlocks();
            for (Block b2 : neighbors) {
                if (b2 != null && (flying || b2.isTraversable())) {
                    update_distances(b1, b2, flying);
                }
            }
        }
        return result(bstart, listEnd, flying);
    }

    private LinkedList<Block> result(Block bstart, LinkedList<Block> listEnd, boolean flying) {
        LinkedList<Block> blocksPath = new LinkedList<>();
        Block b = bstart;
        blocksPath.addFirst(b);
        while (!isGoalReched(b, listEnd)) {
            b = tabPredecessors[b.Y][b.X];
            if (b == null || b.distance == -1 || (!flying && !b.isTraversable())) {
                return null;
            }
            blocksPath.addLast(b);
        }
        return blocksPath;
    }

    private boolean isGoalReched(Block b, LinkedList<Block> listEnd) {
        for (Block block : listEnd) {
            if (block == b) {
                return true;
            }
        }
        return false;
    }

}
