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
public class ListElementPosition implements java.io.Serializable {

    private static final long serialVersionUID = 923928L;

    private LinkedList<ElementPosition> listElementPosition = new LinkedList<>();

    public ListElementPosition() {
    }

    public void add(ElementPosition elementPosition) {
        listElementPosition.add(elementPosition);
    }

    public LinkedList<ElementPosition> getList() {
        return listElementPosition;
    }

}
