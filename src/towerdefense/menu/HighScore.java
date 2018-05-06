/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.menu;

/**
 *
 * @author dduong
 */
public class HighScore implements java.io.Serializable {

    private static final long serialVersionUID = 35271L;

    private final String name;
    private final int score;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    @Override
    public String toString() {
        return "Nom : " + this.name + " || Score : " + this.score;
    }

    public boolean isHigher(HighScore highscore) {
        if (highscore == null) {
            return true;
        }
        return this.score > highscore.getScore();
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public int getScore() {
        return this.score;
    }

    public String getName() {
        return name;
    }

}
