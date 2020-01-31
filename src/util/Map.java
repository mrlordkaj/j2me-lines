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
public class Map {
    private Vector pair;
    
    public Map() {
        pair = new Vector();
    }
    
    public void put(int key, int val) {
        Pair newPair = new Pair(key, val);
        pair.addElement(newPair);
    }
    
    public int get(int key) {
        for(int i = 0; i < pair.size(); i++) {
            Pair curPair = (Pair)pair.elementAt(i);
            if(curPair.key == key) {
                return curPair.val;
            }
        }
        throw new RuntimeException("Key not found!");
    }
    
    class Pair {
        private int key, val;
        
        public Pair(int _key, int _val) {
            key = _key;
            val = _val;
        }
    }
}
