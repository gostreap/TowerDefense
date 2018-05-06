/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.attacker;

import basicobject.Point;
import javafx.scene.paint.Color;
import towerdefense.model.*;

/**
 *
 * @author aissatou
 */
public class FlyingUnit extends Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 103L;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public FlyingUnit(Point position, Board b, int wave) {
        super(position, 3, 30, b, Color.BLUEVIOLET, wave, 2, (int) (Block.getSize() / 2.5)); //Les valeurs seront à déterminer plus tard
    }

    //public void move(){}
    public boolean isFlyingUnit() {
        return true;
    }

}
