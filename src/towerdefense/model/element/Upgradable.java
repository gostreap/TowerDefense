/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.model.element;

import java.util.LinkedList;
import towerdefense.model.Player;

/**
 *
 * @author omar
 */
public interface Upgradable {

    public boolean upgrade(Player p, int stat);

    public LinkedList<String[]> getStatList();

    public int getCostUpgrade(int stat);
}
