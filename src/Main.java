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

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * @author Thinh Pham
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
