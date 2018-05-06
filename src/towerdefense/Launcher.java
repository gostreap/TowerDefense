/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense;

import towerdefense.controller.Main;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author tristan
 */
public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main main = new Main(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
