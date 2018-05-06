/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicobject;

import java.io.IOException;
import javafx.geometry.Dimension2D;

/**
 *
 * @author tristan
 */
public class Dimension implements java.io.Serializable {

    private static final long serialVersionUID = 2L;

    private transient Dimension2D dimension;

    public Dimension(double width, double height) {
        dimension = new Dimension2D(width, height);
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeDouble(this.getWidth());
        out.writeDouble(this.getHeight());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        dimension = new Dimension2D(in.readDouble(), in.readDouble());
    }

    public String toString() {
        return dimension.toString();
    }
}
