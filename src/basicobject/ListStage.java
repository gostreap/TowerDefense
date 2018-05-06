/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicobject;

import java.util.LinkedList;

/**
 *
 * @author omar
 */
public class ListStage implements java.io.Serializable {

    private LinkedList<StageSave> stageList = new LinkedList<>();

    public ListStage() {
    }

    /*--------------------------------------------------------------------------
    -------------------------------- METHODS -----------------------------------
    --------------------------------------------------------------------------*/
    public void add(StageSave stageSave) {
        stageList.add(stageSave);
    }

    public void remove(int level) {
        stageList.remove(level);
    }

    public StageSave getStage(int level) {
        return stageList.get(level);
    }

    public void updateHighScore(int level, int score) {
        StageSave stageSave = getStage(level);
        stageSave.setHightScore(score);
    }

    public boolean canPlay(int level) {
        if (level == 0) {
            return true;
        }
        StageSave previousStage = getStage(level - 1);
        StageSave stage = getStage(level);
        int previousHightScore = previousStage.getHightScore();
        int scoreNeed = stage.getScoreNeed();
        if (previousHightScore >= scoreNeed) {
            return true;
        }
        return false;
    }

    public int size() {
        return stageList.size();
    }

}
