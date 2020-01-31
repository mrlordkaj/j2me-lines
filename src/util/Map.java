/*
 * Copyright (C) 2012 Thinh Pham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package util;

import java.util.Vector;

/**
 *
 * @author Thinh Pham
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
