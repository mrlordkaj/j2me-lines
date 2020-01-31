/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * @author Thinh
 */
public class Main extends MIDlet {
    public static final int SCREENSIZE_WIDTH = 240;
    public static final int SCREENSIZE_HEIGHT = 400;
    public static final String RMS = "lines";
    
    private Game game;
    private Form menu;
    public Display display;

    public void startApp() {
        try {
            RecordStore rs = RecordStore.openRecordStore(RMS, true);
            if (rs.getNumRecords() != 1) {
                rs.closeRecordStore();
                RecordStore.deleteRecordStore(RMS);

                rs = RecordStore.openRecordStore(RMS, true);
                byte[] writer = "0".getBytes();
                rs.addRecord(writer, 0, writer.length);
                rs.closeRecordStore();
            }
        } catch (RecordStoreException ex) {
        }
        
        display = Display.getDisplay(this);
        
        game = new Game(this);
        display.setCurrent(game);
    }
    
    public void newGame() {
        game.newGame();
        backToGame();
    }
    
    public void showMenu() {
        menu = new Menu(this);
        display.setCurrent(menu);
    }
    
    public void gotoLeaderboard() {
        
    }
    
    public void backToGame() {
        game.resume();
        display.setCurrent(game);
        menu = null;
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
