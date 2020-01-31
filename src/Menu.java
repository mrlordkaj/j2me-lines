/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Thinh
 */
public class Menu extends Form implements CommandListener {
    private Main parent;
    private Alert alert;
    
    public Menu(Main _parent) {
        super("Game Help");
        parent = _parent;
        addCommand(new Command("Back", Command.BACK, 1));
        addCommand(new Command("New Game", Command.SCREEN, 1));
        //addCommand(new Command("About", Command.HELP, 1));
        addCommand(new Command("Quit", Command.EXIT, 1));
        setCommandListener(this);
        
        append("Lines 98 Classic\r\nVerion 1.0\r\nDeveloped by Openitvn Forum.\r\n\r\nRULES OF THE GAME:\r\n\r\n" +
                "The objective is to score as many points as possible by making balls of one color form various shapes.\r\n\r\n" +
                "Use the mouse to move the balls. First select the ball to be moved, then an empty destination square. If the destination square is occupied by another ball, that ball is now selected to be moved.\r\n\r\n" +
                "Note that you cannot always place the ball where you'd want to.\r\n\r\n" +
                "After you move a ball, three more appear, except when you formed a shape.\r\n\r\n" +
                "The more balls in a shape, the more points you score for making it.\r\n\r\n" +
                "For more infomation and games, please visit us at http://openitvn.net\r\n\r\n" + 
                "THANKS FOR YOUR PLAYING!");
    }
    
    public void commandAction(Command c, Displayable d) {
        switch(c.getCommandType()) {
            case Command.BACK:
                parent.backToGame();
                break;
                
            case Command.SCREEN:
                parent.newGame();
                break;
                
            case Command.HELP:
                alert = new Alert("About", "Classic Lines\r\nVersion 1.0\r\n\r\nDeveloped by Openitvn", null, AlertType.INFO);
                alert.setTimeout(Alert.FOREVER);
                parent.display.setCurrent(alert, this);
                break;
                
            case Command.EXIT:
                parent.notifyDestroyed();
                break;
        }
        alert = null;
    }
}
