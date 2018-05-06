/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Point;
import towerdefense.model.Block;

/**
 *
 * @author dduong
 */
public class BulletBombView extends BulletView {

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletBombView(BoardView boardView) {
        super(Point.ZERO, 3 * Block.getSize() / 20, boardView);
    }

}
