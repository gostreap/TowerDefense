/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author omar
 */
public class Clock extends StackPane {

    private double timePreviousPause = System.currentTimeMillis();
    private double totalTime = 0;
    private Text time = new Text("");
    private Rectangle rect = new Rectangle(50, 50);

    public Clock() {
        rect.setFill(Color.TRANSPARENT);
        super.getChildren().addAll(rect, time);
        StackPane.setAlignment(time, Pos.CENTER);
        time.toFront();
    }

    public void updateTimeFromBegining() {
        totalTime += System.currentTimeMillis() - timePreviousPause;
        //System.out.println(System.currentTimeMillis() - timePreviousPause);
        //System.out.println(totalTime);
        timePreviousPause = System.currentTimeMillis();
        int[] timeTab = new int[3];
        timeTab[0] = ((int) totalTime / 1000);
        timeTab[1] = (timeTab[0] / 60);
        timeTab[2] = timeTab[1] / 60;
        timeTab[0] %= 60;
        timeTab[1] %= 60;
        String acc = "";
        for (int i = 0; i < timeTab.length; i++) {
            if (timeTab[i] != 0 || !isAllAfterPosNull(timeTab, i)) {
                String s = fillWithZero(Integer.toString(timeTab[i]));
                if (i != 0) {
                    s = s + " : ";
                }
                acc = s + acc;
            }
        }
        if (acc.equals("")) {
            acc = "00";
        }
        time.setText(acc);
        time.toFront();
    }

    public String fillWithZero(String s) {
        if (s.length() == 1) {
            return "0" + s;
        }
        return s;
    }

    public boolean isAllAfterPosNull(int[] tab, int pos) {
        for (int i = pos; i < tab.length; i++) {
            if (tab[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public void updateTimePreviousPause() {
        this.timePreviousPause = System.currentTimeMillis();
    }

}
