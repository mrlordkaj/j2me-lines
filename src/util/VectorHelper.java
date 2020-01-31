/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Vector;

/**
 *
 * @author Thinh
 */
public class VectorHelper extends Vector {
    
    public VectorHelper() {
        super();
    }
    
    public void addAll(VectorHelper plus) {
        for(int i = 0; i < plus.size(); i++) {
            this.addElement(plus.elementAt(i));
        }
    }
    
    public void reverse() {
        VectorHelper temp = new VectorHelper();
        for(int i = this.size() - 1; i >= 0; i--) {
            temp.addElement(this.elementAt(i));
        }
        this.removeAllElements();
        for(int i = 0; i < temp.size(); i++) {
            this.addElement(temp.elementAt(i));
        }
    }
}
