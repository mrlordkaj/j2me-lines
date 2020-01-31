package util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Thinh
 */
public class GraphicButton {
    private Sprite sprite;
    private int x, y, width, height;
    private byte cmd;
    public boolean active = false;
    
    public GraphicButton(String imagePath, byte _cmd, int _x, int _y, int _width, int _height) {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        cmd = _cmd;
        sprite = new Sprite(ImageHelper.loadImage(imagePath), width, height);
    }
    
    public GraphicButton(Image img, byte _cmd, int _x, int _y, int _width, int _height) {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        cmd = _cmd;
        sprite = new Sprite(img, width, height);
    }
    
    public byte getCommand() { return cmd; }
    
    public void paint(Graphics g) {
        sprite.setFrame(active?1:0);
        sprite.setPosition(x, y);
        sprite.paint(g);
    }
    
    public boolean contains(int _x, int _y) {
        if(_x > x && _x < x + width && _y > y && _y < y + height) return true;
        return false;
    }
}
