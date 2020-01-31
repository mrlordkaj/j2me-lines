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

/**
 *
 * @author Thinh Pham
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
