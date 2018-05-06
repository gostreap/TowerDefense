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
public class Basic extends Attacker implements java.io.Serializable {

    private static final long serialVersionUID = 101L;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public Basic(Point position, Board b, int wave) {
        super(position, 2, 100, b, Color.CADETBLUE, wave, 1, (int) (Block.getSize() / 2.5));
    }

}
