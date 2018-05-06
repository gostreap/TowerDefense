/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.controller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tristan
 */
public class SoundController {

    private static AudioClip POP1;

    private static AudioClip EXPLOSION1;

    private static AudioClip SHOOT1;
    private static AudioClip SHOOT2;
    private static AudioClip SHOOT3;
    private static AudioClip SHOOT4;

    private static AudioClip CLICK1;

    private static Random random = new Random();

    private static boolean mute = false;

    /*--------------------------------------------------------------------------
    -----------------------------CONSTRUCTORS-----------------------------------
    --------------------------------------------------------------------------*/
    static {
        try {
            File file = new File("src/sounds/pop1.wav");
            POP1 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/explosion1.wav");
            EXPLOSION1 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/shoot1.wav");
            SHOOT1 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/shoot2.wav");
            SHOOT2 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/shoot3.wav");
            SHOOT3 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/shoot4.wav");
            SHOOT4 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            File file = new File("src/sounds/click1.wav");
            CLICK1 = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SoundController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------METHODS----------------------------------------
    --------------------------------------------------------------------------*/
    public static void POP() {
        if (!mute) {
            int i = random.nextInt(1) + 1;
            switch (i) {
                case 1:
                    POP1.play();
                    break;
            }
        }
    }

    public static void EXPLOSION() {
        if (!mute) {
            EXPLOSION1.play();
        }
    }

    public static void CLICK() {
        if (!mute) {
            CLICK1.play();
        }
    }

    public static void SHOOT() {
        if (!mute) {
            int i = random.nextInt(4) + 1;
            switch (i) {
                case 1:
                    SHOOT1.play();
                    break;
                case 2:
                    SHOOT2.play();
                    break;
                case 3:
                    SHOOT3.play();
                    break;
                case 4:
                    SHOOT4.play();
                    break;
            }
        }
    }

    /*--------------------------------------------------------------------------
    -----------------------------GETTERS----------------------------------------
    --------------------------------------------------------------------------*/
    public static void toggleMute() {
        mute = !mute;
    }

    public static String getText() {
        if (!mute) {
            return "SON ACTIVÉ";
        } else {
            return "SON DÉSACTIVÉ";
        }
    }
}
