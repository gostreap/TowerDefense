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
public class Fast extends Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 102L;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Fast(Point position, Board b, int wave) {
        super(position, 4, 20, b, Color.DARKORANGE, wave, 1, (int) (Block.getSize() / 3)); //Les valeurs seront à déterminer plus tard
    }

}
