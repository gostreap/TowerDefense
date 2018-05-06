/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package towerdefense.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author omar
 */
public class HeartImage {

    private BufferedImage heartImage;
    private Image image;

    public HeartImage() {
        image = new Image("file:src/images/heart.png");
        heartImage = SwingFXUtils.fromFXImage(image, null);
    }

    public void printImage() {
        for (int i = 0; i < heartImage.getWidth(); i++) {
            for (int j = 0; j < heartImage.getHeight(); j++) {
                if (heartImage.getRGB(j, i) == 0) {
                    System.out.print(" ");
                } else if (heartImage.getRGB(j, i) == Color.RED.getRGB()) {
                    System.out.print("R");
                } else {
                    System.out.print("-");
                }
            }
            System.out.println("");
        }
    }

    public void updateColor(double percentLife) {
        modifierPixelEnRouge((int) (heartImage.getHeight() * percentLife));
    }

    public void modifierPixelEnRouge(int nombreDeLigne) {
        Color red = Color.RED;
        int redCode = red.getRGB();
        for (int j = heartImage.getHeight() - 1; j >= heartImage.getHeight()
                - nombreDeLigne && j >= 0; j--) {

            for (int i = 0; i < heartImage.getWidth(); i++) {
                int rgb = heartImage.getRGB(i, j); //always returns TYPE_INT_ARGB
                int alpha = (rgb >> 24) & 0xFF;
                if (alpha <= 110 && estALinterieur(i, j)) {
                    heartImage.setRGB(i, j, redCode);
                }
            }
        }
    }

    public boolean estALinterieur(int i, int j) {
        int x = i;
        int y = j;
        do {
            if (x + 1 < heartImage.getWidth()) {
                x++;
            } else {
                return false;
            }
        } while (heartImage.getRGB(x, y) == 0);
        x = i;
        y = j;
        do {
            if (x - 1 >= 0) {
                x--;
            } else {
                return false;
            }
        } while (heartImage.getRGB(x, y) == 0);

        x = i;
        y = j;
        do {
            if (y - 1 >= 0) {
                y--;
            } else {
                return false;
            }

        } while (heartImage.getRGB(x, y) == 0);
        x = i;
        y = j;
        do {
            if (y + 1 < heartImage.getHeight()) {
                y++;
            } else {
                return false;
            }
        } while (heartImage.getRGB(x, y) == 0);

        return true;

    }

    public Image getFXImage() {
        return SwingFXUtils.toFXImage(heartImage, null);
    }

    public static void main(String[] args) {
        HeartImage heartImage = new HeartImage();
        heartImage.printImage();
        heartImage.modifierPixelEnRouge(10);
        heartImage.printImage();
    }
}
