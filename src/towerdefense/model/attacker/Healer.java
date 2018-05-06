/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.attacker;

import basicobject.Point;
import java.util.LinkedList;
import javafx.scene.paint.Color;
import towerdefense.model.*;

/**
 *
 * @author aissatou
 */
public class Healer extends Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 104L;

    private final double range;
    private double lastHeal = System.currentTimeMillis();

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Healer(Point position, Board b, int wave) {
        super(position, 2, 40, b, Color.AQUAMARINE, wave, 1, (int) (Block.getSize() / 2.5)); //Les valeurs seront à déterminer plus tard
        range = 5;
    }

    @Override
    public void effect(LinkedList<Attacker> attackers) {
        super.effect(attackers);
        heal(attackers);
    }

    public void heal(LinkedList<Attacker> attackers) {
        boolean canHeal = System.currentTimeMillis() - lastHeal > 300 * board.getTimeCoef();
        if (canHeal) {
            lastHeal = System.currentTimeMillis();
            LinkedList<Attacker> toheal = detectFriends(attackers);
            for (Attacker attacker : toheal) {
                if (attacker != this) {
                    attacker.healMe(20);
                }
            }
        }
    }

    public LinkedList<Attacker> detectFriends(LinkedList<Attacker> attackers) {
        LinkedList<Attacker> attackers_in_range = new LinkedList<>();

        for (Attacker attacker : attackers) {
            double distance = attacker.getPosition().distance(position);
            if (distance < range * Block.getSize()) {
                attackers_in_range.add(attacker);
            }
        }
        return attackers_in_range;
    }

}
