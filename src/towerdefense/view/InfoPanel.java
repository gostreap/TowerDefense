/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import java.util.LinkedList;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import towerdefense.model.element.Element;
import towerdefense.model.element.Upgradable;

/**
 *
 * @author tristan
 */
public class InfoPanel extends VBox {

    private final Element element;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public InfoPanel(Element element) {
        this.setAlignment(Pos.CENTER);
        this.element = element;
        initPane();
    }

    private void initPane() {

        Text name = new Text(element.getElementName());
        this.getChildren().add(name);

        if (element instanceof Upgradable) {
            LinkedList<String[]> stat_LinkedList
                    = ((Upgradable) element).getStatList();

            for (int i = 0; i < stat_LinkedList.size(); i++) {
                String[] tabStrings = stat_LinkedList.get(i);
                Text text = new Text(tabStrings[0] + " : " + tabStrings[1]);
                this.getChildren().add(text);
            }
        }

        Text cost = new Text(Integer.toString(element.COST) + " $");
        this.getChildren().add(cost);
    }

}
