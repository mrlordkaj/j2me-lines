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

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import util.GraphicButton;
import util.ImageHelper;
import util.StringHelper;

/**
 *
 * @author Thinh Pham
 */
public class Game extends GameCanvas implements Runnable, CommandListener {
    public static final int PADDING_LEFT = 1;
    public static final int PADDING_TOP = 108;
    private static final byte COMMAND_NONE = 0;
    private static final byte COMMAND_QUIT = 1;
    private static final byte COMMAND_MENU = 2;
    private static final byte COMMAND_STEPBACK = 3;
    
    public Image backgroundImage, stepbackDisabledImage;
    
    private byte activeCommand = COMMAND_NONE;
    
    private boolean pageLooping = true, isLoading = true, isPaused = false;
    private boolean buttonTouching = false, boardTouching = false;
    private short schedule = 40;
    private int second = 0, timeline = 0;
    private GraphicButton[] button;
    
    private String score = "00000", record = "00000", time = "00:00:00";
    
    private Board board;
    
    private Main parent;
    private Alert alert;
    
    public Game(Main _parent) {
        super(false);
        setFullScreenMode(true);
        parent = _parent;
        
        board = new BoardLines(this);
        
        prepareResource();
        
        new Thread(this).start();
        
        board.newBoard();
    }
    
    public void newGame() {
        score = "00000";
        time = "00:00:00";
        second = 0;
        board.clearBoard();
        board.newBoard();
    }
    
    private void prepareResource() {
        board.prepareResource();
        
        button = new GraphicButton[] {
            new GraphicButton("/images/btnpower.png", COMMAND_QUIT, 188, 0, 50, 50),
            new GraphicButton("/images/btnmenu.png", COMMAND_MENU, 3, 350, 50, 50),
            new GraphicButton("/images/btnback.png", COMMAND_STEPBACK, 188, 350, 50, 50),
        };
        
        backgroundImage = ImageHelper.loadImage("/images/board.png");
        stepbackDisabledImage = ImageHelper.loadImage("/images/btnback_disabled.png");
        
        isLoading = false;
    }
    
    protected void hideNotify() {
        isPaused = true;
    }
    
    public void resume() {
        isPaused = false;
    }

    public void run() {
        while(pageLooping) {
            update();
            repaint();
            try {
                Thread.sleep(schedule);
            } catch (InterruptedException ex) {}
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(0x000000);
        g.fillRect(0, 0, Main.SCREENSIZE_WIDTH, Main.SCREENSIZE_HEIGHT);
        
        if(isLoading) return;
        
        if(isPaused) {
            g.setColor(0xffffff);
            g.drawString("PAUSED", Main.SCREENSIZE_WIDTH / 2, Main.SCREENSIZE_HEIGHT / 2, Graphics.HCENTER | Graphics.BASELINE);
            g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
            g.drawString("Tap to continue.", Main.SCREENSIZE_WIDTH / 2, Main.SCREENSIZE_HEIGHT / 2 + 16, Graphics.HCENTER | Graphics.BASELINE);
            return;
        }
        
        //vẽ nền
        g.drawImage(backgroundImage, 0, 0, Graphics.LEFT | Graphics.TOP);
        
        //vẽ các nút bấm
        button[0].paint(g);
        button[1].paint(g);
        if(board.haveStepback()) button[2].paint(g);
        else g.drawImage(stepbackDisabledImage, 188, 350, Graphics.LEFT | Graphics.TOP);
        
        //vẽ bàn cờ
        board.paint(g);
        
        //vẽ điểm
        g.setColor(0x00deff);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
        g.drawString(record, 64, 73, Graphics.LEFT | Graphics.BASELINE);
        g.drawString(score, 64, 94, Graphics.LEFT | Graphics.BASELINE);
        g.drawString(time, Main.SCREENSIZE_WIDTH / 2, 390, Graphics.HCENTER | Graphics.BASELINE);
    }
    
    protected void update() {
        if(isPaused) return;
        
        if(++timeline > 24) {
            second++;
            timeline = 0;
            time = StringHelper.parseTime(second);
        }
        board.update();
    }
    
    protected void pointerPressed(int x, int y) {
        if(isPaused) {
            isPaused = false;
            return;
        }
        
        if(x > 3 && x < 238 && y > 109 && y < 344) {
            board.click(x, y);
            boardTouching = true;
        }
        else {
            buttonTouching = true;
            setActiveButton(x, y);
        }
    }
    
    protected void pointerDragged(int x, int y) {
        if(buttonTouching) {
            setActiveButton(x, y);
        }
    }
    
    protected void pointerReleased(int x, int y) {
        boardTouching = false;
        buttonTouching = false;
        for(int i = 0; i < 3; i++) {
            button[i].active = false;
        }
        
        switch(activeCommand) {
            case COMMAND_QUIT:
                alert = new Alert("Quit", "Are you sure you want to quit?", null, AlertType.WARNING);
                alert.addCommand(new Command("Ok", Command.EXIT, 1));
                alert.addCommand(new Command("Cancel", Command.CANCEL, 1));
                alert.setCommandListener(this);
                parent.display.setCurrent(alert, this);
                break;
                
            case COMMAND_MENU:
                parent.showMenu();
                break;
                
            case COMMAND_STEPBACK:
                board.doStepback();
                break;
        }
        
        activeCommand = COMMAND_NONE;
    }
    
    //kiểm tra xem nút lệnh nào đang được active
    private void setActiveButton(int x, int y) {
        for(int i = 0; i < 3; i++) {
            button[i].active = false;
        }
        
        if(button[0].contains(x, y)) {
            button[0].active = true;
            activeCommand = button[0].getCommand();
            return;
        }
        
        if(button[1].contains(x, y)) {
            button[1].active = true;
            activeCommand = button[1].getCommand();
            return;
        }
        
        if(board.haveStepback() && button[2].contains(x, y)) {
            button[2].active = true;
            activeCommand = button[2].getCommand();
            return;
        }
        
        activeCommand = COMMAND_NONE;
    }

    public void commandAction(Command c, Displayable d) {
        switch(c.getCommandType()) {
            case Command.EXIT:
                parent.notifyDestroyed();
                break;
                
            case Command.CANCEL:
                parent.display.setCurrent(this);
                break;
                
            case Command.OK:
                parent.display.setCurrent(this);
                newGame();
                break;
        }
        alert = null;
    }
    
    public void gameOver() {
        alert = new Alert("Game over!", "Congratulation! You done with " + second + " seconds and " + board.getScore() + " points!", null, AlertType.CONFIRMATION);
        alert.setTimeout(Alert.FOREVER);
        alert.addCommand(new Command("New game", Command.OK, 1));
        alert.setCommandListener(this);
        parent.display.setCurrent(alert, this);
    }
    
    public void updateScore(int score) {
        StringBuffer bufScore = new StringBuffer();
        String strScore = Integer.toString(score);
        int numberOfZero = 5 - strScore.length();
        for(int i = 0; i < numberOfZero; i++) {
            bufScore.append("0");
        }
        bufScore.append(strScore);
        this.score = bufScore.toString();
    }
    
    public void updateRecord(int record) {
        StringBuffer bufScore = new StringBuffer();
        String strScore = Integer.toString(record);
        int numberOfZero = 5 - strScore.length();
        for(int i = 0; i < numberOfZero; i++) {
            bufScore.append("0");
        }
        bufScore.append(strScore);
        this.record = bufScore.toString();
    }
}
