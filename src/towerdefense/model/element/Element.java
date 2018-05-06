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
import basicobject.ColorN;
import basicobject.Dimension;
import basicobject.Point;
import java.util.LinkedList;
import java.util.Random;
import javafx.scene.paint.Color;
import towerdefense.model.Block;
import towerdefense.model.Board;
import towerdefense.model.attacker.Attacker;
import towerdefense.view.ElementView;

public abstract class Element implements java.io.Serializable {

    private static final long serialVersionUID = 200L;

    protected Board board;
    protected int range;
    protected boolean flying_unit;
    protected Dimension dimension;
    protected Block block;
    protected transient ElementView elementView;
    protected transient Color color;
    protected ColorN colorN;
    private int findMode;
    public final int COST;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Element(Dimension dimension, Block block, Board board, int cost,
            Color color) {
        this(dimension, block, board, 0, false, cost, color);
    }

    public Element(Dimension dimension, Block block, Board board, int range,
            int cost, Color color) {
        this(dimension, block, board, range, false, cost, color);
    }

    public Element(Dimension dimension, Block block, Board board, int range,
            boolean flying_unit, int cost, Color color) {
        this.dimension = dimension;
        this.block = block;
        this.range = range;
        this.flying_unit = flying_unit;
        this.board = board;
        this.COST = cost;
        this.color = color;
        this.colorN = new ColorN(color);
        this.findMode = 0;
    }

    /*--------------------------------------------------------------------------
    ----------------------------METHODS-----------------------------------------
    --------------------------------------------------------------------------*/
    //Inverse width et height (On l'utilise dans le placement des tours quand
    //on appuie sur espace)
    public void switchDimension() {
        dimension = new Dimension(dimension.getHeight(), dimension.getWidth());
        elementView.setDimension(dimension);
    }

    public void resize(double factor) { //factor nécessaire dans tower
        elementView.resize(block);
    }

    public abstract void effect(LinkedList<Attacker> attackers);

    public abstract void resetElementView();

    public void remove() {
        board.getPlayer().addMoney(COST / 2);
        if (this instanceof Tower) {
            ((Tower) this).removeBullets();
        }
        if (this instanceof BoostTower) {
            ((BoostTower) this).removeBoost();
        }
        board.removeElement(this);
        elementView.removeOfBoard();
    }

    public void upStat(int stat) {
        return;
    }

    public void increaseFindMode() {
        findMode = (findMode + 1) % 5;
    }

    /*--------------------------------------------------------------------------
    -------------------------FIND ATTACKERS-------------------------------------
    --------------------------------------------------------------------------*/
    public Attacker findTargetAttacker(LinkedList<Attacker> attackers) {
        switch (this.findMode) {
            case 0:
                return findNearestAttacker(attackers);
            case 1:
                return findClosestFromSpawnAttacker(attackers);
            case 2:
                return findRandomAttacker(attackers);
            case 3:
                return findStrongestAttacker(attackers);
            case 4:
                return findWeakestAttacker(attackers);
        }
        return null;
    }

    // On recherche l'attaquant le plus proche
    private Attacker findNearestAttacker(LinkedList<Attacker> attackers) {
//        System.out.println("Nearest");
        if (attackers.isEmpty()) {
            return null;
        }

        Attacker nearest_attacker = null;
        double plus_courte_distance = Double.POSITIVE_INFINITY;

        for (Attacker attacker : attackers) {

            //Verifie que la tour à le droit de tirer sur cette attaquant
            if (!canShoot(attacker)) {
                continue;
            }

            //Recherche classique du min
            double distance_attacker_courant = attacker.getPosition()
                    .distance(getCenterPosition());

            if (distance_attacker_courant < plus_courte_distance) {
                plus_courte_distance = distance_attacker_courant;
                nearest_attacker = attacker;
            }
        }

        if (isInRange(nearest_attacker)) {
            return nearest_attacker;
        }
        return null;
    }

    // On recherche l'attaquant ayant le plus de vie à portée
    private Attacker findStrongestAttacker(LinkedList<Attacker> attackers) {
//        System.out.println("Strongest");
        if (attackers.isEmpty()) {
            return null;
        }

        Attacker strongest_attacker = null;
        double maxHealthPoint = -1;

        for (Attacker attacker : attackers) {

            //Verifie que la tour à le droit de tirer sur cette attaquant
            if (!canShoot(attacker)) {
                continue;
            }

            //Recherche classique du max
            double currentHealthPoint = attacker.getHealthPoint();

            if (currentHealthPoint > maxHealthPoint) {
                maxHealthPoint = currentHealthPoint;
                strongest_attacker = attacker;
            }
        }

        if (isInRange(strongest_attacker)) {
            return strongest_attacker;
        }
        return null;
    }

    // On recherche l'attaquant ayant le moins de vie à portée
    private Attacker findWeakestAttacker(LinkedList<Attacker> attackers) {
        if (attackers.isEmpty()) {
            return null;
        }

        Attacker weakest_attacker = null;
        double minHealthPoint = Double.POSITIVE_INFINITY;

        for (Attacker attacker : attackers) {

            //Verifie que la tour à le droit de tirer sur cette attaquant
            if (!canShoot(attacker)) {
                continue;
            }

            //Recherche classique du min
            double currentHealthPoint = attacker.getHealthPoint();

            if (currentHealthPoint < minHealthPoint) {
                minHealthPoint = currentHealthPoint;
                weakest_attacker = attacker;
            }
        }

        if (isInRange(weakest_attacker)) {
            return weakest_attacker;
        }
        return null;
    }

    // Renvoie un attaquant à porté au hasard
    private Attacker findRandomAttacker(LinkedList<Attacker> attackers) {
        if (attackers.isEmpty()) {
            return null;
        }

        int comptor = 0;
        while (comptor < 15) { //15 est une valeur arbitraire
            Random rand = new Random();
            int index = rand.nextInt(attackers.size());
            Attacker randomAttacker = attackers.get(index);
            //Verifie que la tour à le droit de tirer sur cette attaquant
            if (!canShoot(randomAttacker)) {
                comptor++;
                continue;
            }
            if (isInRange(randomAttacker)) {
                return randomAttacker;
            }
            comptor++;
        }
        return null;
    }

    // On cherche un des attaquants les plus proches d'un château
    private Attacker findClosestFromSpawnAttacker(LinkedList<Attacker> attackers) {
        if (attackers.isEmpty()) {
            return null;
        }

        Attacker closest_attacker = null;
        int minBlockDistance = Integer.MAX_VALUE;

        for (Attacker attacker : attackers) {

            //Verifie que la tour à le droit de tirer sur cette attaquant
            if (!canShoot(attacker)) {
                continue;
            }

            //Recherche classique du min
            int currentBlockDistance = attacker.getBlockDistanceToSpawn();

            if (currentBlockDistance < minBlockDistance) {
                minBlockDistance = currentBlockDistance;
                closest_attacker = attacker;
            }
        }

        if (isInRange(closest_attacker)) {
            return closest_attacker;
        }
        return null;
    }

    private boolean canShoot(Attacker attacker) {
        return (attacker.isFlyingUnit() && flying_unit)
                || (!attacker.isFlyingUnit() && !flying_unit);
    }

    protected boolean isInRange(Attacker attacker) {
        if (attacker == null) {
            return false;
        }
        double distance_attacker_courant = attacker.getPosition()
                .distance(getCenterPosition());

        return distance_attacker_courant < range * Block.getSize()
                + attacker.getTaille();
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public abstract String getElementName();

    public int getCoefficient() {
        return 0;
    }

    public Point getCenterPosition() {
        Point position = block.getPosition();
        return new Point(
                position.getX() + (dimension.getWidth() * Block.getSize()) / 2,
                position.getY() + (dimension.getHeight() * Block.getSize()) / 2
        );
    }

    public Block getBlock() {
        return block;
    }

    public ElementView getElementView() {
        return elementView;
    }

    public int getRange() {
        return range;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Board getBoard() {
        return board;
    }

    public String getModeString() {
        String[] tabModeStrings = {"Le plus proche de la tour", "Le plus proche de l'arrivée", "Aléatoire", "Le plus fort", "Le plus faible"};
        return tabModeStrings[findMode];
    }

}
