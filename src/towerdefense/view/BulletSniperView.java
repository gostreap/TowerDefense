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
public class BulletSniperView extends BulletView {

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletSniperView(BoardView boardView) {
        super(Point.ZERO, 2 * Block.getSize() / 20, boardView);
    }

}
