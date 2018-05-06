/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import basicobject.Point;
import javafx.scene.paint.Color;

/**
 *
 * @author dduong
 */
public class BulletBombExplosionView extends BulletView {

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public BulletBombExplosionView(Point point, BoardView boardView) {
        super(point, 1.0, boardView);
        setFill(Color.RED);
        setOpacity(0.3);
//        setStyle("-fx-stroke:red;"+"-fx-stroke-width:3;");
    }

}
