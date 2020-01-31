/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Thinh
 */
public class Rectangle {
    public int x, y, width, height;
    
    public Rectangle() {
        
    }
    
    public Rectangle(int _x, int _y, int _width, int _height) {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
    }
    
    public boolean contains(int _x, int _y) {
        if(_x >= x && _x <= x + width) {
            if(_y >= y && _y <= y + height) return true;
        }
        return false;
    }
}
