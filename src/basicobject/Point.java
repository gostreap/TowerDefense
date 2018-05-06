/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicobject;

import java.io.IOException;
import javafx.geometry.Point2D;

/**
 *
 * @author tristan
 */
public class Point implements java.io.Serializable {

    private static final long serialVersionUID = 3L;

    public static Point ZERO = new Point(0, 0);

    private Point2D point;

    public Point(double x, double y) {
        point = new Point2D(x, y);
    }

    private Point(Point2D point) {
        this(point.getX(), point.getY());
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public Point multiply(double factor) {
        return new Point(point.multiply(factor));
    }

    public Point add(Point p) {
        return new Point(point.add(p.point));
    }

    public double distance(Point p) {
        return point.distance(p.point);
    }

    public double magnitude() {
        return point.magnitude();
    }

    public double angle(Point p) {
        return point.angle(p.point);
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeDouble(this.getX());
        out.writeDouble(this.getY()
        );
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        point = new Point2D(in.readDouble(), in.readDouble());
    }

    @Override
    public String toString() {
        return "Point : X " + getX() + " Y " + getY();
    }

}
