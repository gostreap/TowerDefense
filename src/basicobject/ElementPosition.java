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
public class ElementPosition implements java.io.Serializable {

    private static final long serialVersionUID = 7283729L;

    public Dimension dimension;
    public Point position;
    public String elementName;

    public ElementPosition(Dimension dimension, Point position, String elementName) {
        this.dimension = dimension;
        this.position = position;
        this.elementName = elementName;
    }

}
