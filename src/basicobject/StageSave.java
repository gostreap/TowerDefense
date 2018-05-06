/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicobject;

/**
 *
 * @author omar
 */
public class StageSave implements java.io.Serializable {

    private ListElementPosition listElementPosition;
    private int hp;
    private int width;
    private int height;
    private int money;
    private int hightScore;
    private int scoreNeed;

    public StageSave(ListElementPosition listElementPosition, int hp, int width, int height, int money) {
        this.listElementPosition = listElementPosition;
        this.hp = hp;
        this.width = width;
        this.height = height;
        this.money = money;
    }

    public StageSave(ListElementPosition listElementPosition, int hp, int width, int height, int money, int hightScore, int scoreNeed) {
        this(listElementPosition, hp, width, height, money);
        this.hightScore = hightScore;
        this.scoreNeed = scoreNeed;
    }

    /*--------------------------------------------------------------------------
    ------------------------------ GETTERS -------------------------------------
    --------------------------------------------------------------------------*/
    public int getHeight() {
        return height;
    }

    public ListElementPosition getListElementPosition() {
        return listElementPosition;
    }

    public int getHp() {
        return hp;
    }

    public int getMoney() {
        return money;
    }

    public int getWidth() {
        return width;
    }

    public int getHightScore() {
        return hightScore;
    }

    public int getScoreNeed() {
        return scoreNeed;
    }

    public void setHightScore(int hightScore) {
        if (hightScore > this.hightScore) {
            this.hightScore = hightScore;
        }
    }

    public void setPreviousScoreNeed(int scoreNeed) {
        this.scoreNeed = scoreNeed;
    }

}
