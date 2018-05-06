/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

import java.util.LinkedList;

/**
 *
 * @author dduong
 */
public class HighScoreList implements java.io.Serializable {

    private static final long serialVersionUID = 198362L;

    private LinkedList<HighScore> list;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public HighScoreList() {
        this.list = new LinkedList<>();
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public int size() {
        return list.size();
    }

    public int i_insert(HighScore hs) {
        if (list.isEmpty()) {
            return 0;
        } else {
            for (HighScore sc : list) {
                if (hs.isHigher(sc)) {
                    System.out.println(list.indexOf(sc));
                    return list.indexOf(sc);
                }
            }
            return list.size();
        }
    }

    public void insertScore(HighScore hs) {
        int i = i_insert(hs);
        System.out.println(i);
        if (i != -1) {
            list.add(i, hs);
            while (list.size() > 10) {
                list.removeLast();
            }
        }
    }

    public void printlist() {
        for (HighScore sc : list) {
            if (sc != null) {
                System.out.print(sc);
            }
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public String getInfo(int i) {
        if (i < list.size()) {
            return list.get(i).toString();
        }
        return null;
    }

    public HighScore getScore(int i) {
        if (i < list.size()) {
            return list.get(i);
        }
        return null;
    }

}
