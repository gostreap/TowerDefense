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
public class Patapouf extends Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 105L;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Patapouf(Point position, Board b, int wave) {
        super(position, 1.3, 200, b, Color.BROWN, wave, 3, (int) (Block.getSize() / 2)); //Les valeurs seront à déterminer plus tard
    }

}
