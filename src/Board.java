
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import util.ImageHelper;
import util.Map;
import util.Rectangle;
import util.VectorHelper;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Thinh
 */
public abstract class Board {
    private static final byte NUMBER_TYPE = 7;
    private static final byte BALL_SIZE = 23;
    
    public Sprite[] ballSprite;
    public Sprite nextSprite;
    public Image emptyCellImage;
    
    private Rectangle boardArea;
    public int[] cell = new int[82];
    private int[][] cellPosition = new int[81][2];
    public int[] randPos = new int[3], randBall = new int[3];
    private Vector open = new Vector(), close = new Vector();
    private int selBall = -1, selTimeline = 0, destroyTimeline = 0, growTimeline = 0;
    private VectorHelper destroy = new VectorHelper();
    private VectorHelper trackPath = new VectorHelper();
    private boolean haveStepback;
    private int[] stepbackCell = new int[82];
    private int[] stepbackRandPos = new int[3], stepbackRandBall = new int[3];
    protected int score, record;
    private int stepbackScore;
    
    protected Game parent;
    
    public Board(Game _parent) {
        parent = _parent;
        boardArea = new Rectangle();
        boardArea.x = Game.PADDING_LEFT;
        boardArea.y = Game.PADDING_TOP;
        boardArea.width = boardArea.height = 234;
        for (int i = 0; i < 81; i++) {
            cell[i] = 0;
            int col, row;
            col = i % 9;
            if (i == 0) {
                row = 0;
            } else {
                row = (i - col) / 9;
            }
            cellPosition[i][0] = col * (BALL_SIZE + 3) + boardArea.x + 3;
            cellPosition[i][1] = row * (BALL_SIZE + 3) + boardArea.y + 3;
        }
        cell[81] = 8;
    }
    
    public void prepareResource() {
        try {
            RecordStore rs = RecordStore.openRecordStore(Main.RMS, true);
            record = Integer.parseInt(new String(rs.getRecord(1)));
        } catch (RecordStoreException ex) {
            record = 0;
        }
        parent.updateRecord(record);
        
        Image ballImage = ImageHelper.loadImage("/images/balls.png");
        ballSprite = new Sprite[NUMBER_TYPE];
        for(int i = 0; i < NUMBER_TYPE; i++) {
            Image curBallImage = Image.createImage(BALL_SIZE*22, BALL_SIZE);
            Graphics g = curBallImage.getGraphics();
            g.drawImage(ballImage, 0, -i*BALL_SIZE, Graphics.LEFT | Graphics.TOP);
            ballSprite[i] = new Sprite(curBallImage, BALL_SIZE, BALL_SIZE);
        }
        ballImage = null;
        
        nextSprite = new Sprite(ImageHelper.loadImage("/images/next.png"), 25, 25);
        
        emptyCellImage = ImageHelper.loadImage("/images/empty.png");
    }
    
    public abstract VectorHelper checkDestroy(int pos);
    public abstract void addScore(int numBall);
    
    public void update() {
        //qua bong duoc chon se nhay
        if (selBall != -1) {
            if (selTimeline < 6) {
                selTimeline++;
            } else {
                selTimeline = 0;
            }
        }

        //cac qua bong bi no
        if (destroyTimeline >= 7 && destroyTimeline < 15) {
            destroyTimeline++;
        } else {
            for (int i = 0; i < destroy.size(); i++) {
                //neu co bong nho thi giu nguyen no
                int currentDestroy = Integer.parseInt(destroy.elementAt(i).toString());
                cell[currentDestroy] = 0;
                for (int k = 0; k < 3; k++) {
                    if (randPos[k] == currentDestroy) {
                        cell[currentDestroy] = -randBall[k];
                        break;
                    }
                }
            }
            destroyTimeline = 0;
            destroy = new VectorHelper();
        }
        
        //cac qua bong dang lon
        if(growTimeline > 18) growTimeline--;
        else if (growTimeline != 0) {
            growTimeline = 0;
            newTurn();
        }

        if (trackPath.size() > 0) {
            updateMove();
        }
    }
    
    private void updateMove() {
        if (trackPath.size() > 1) {
            int fromPos = Integer.parseInt(trackPath.elementAt(0).toString());
            int toPos = Integer.parseInt(trackPath.elementAt(1).toString());
            swapCell(fromPos, toPos);
            trackPath.removeElementAt(0);
        } else if (trackPath.size() == 1) {
            int toPos = Integer.parseInt(trackPath.elementAt(0).toString());
            destroy = checkDestroy(toPos);
            trackPath = new VectorHelper();

            if (destroy.size() > 0) //kiem tra bong den vi tri moi co an khong
            {
                addScore(destroy.size());
                destroyTimeline = 7;
            } else //neu khong an thi phai kiem tra xem co bong nho khong, neu co thi chuyen no di cho khac, sau do bat dau luot moi
            {
                for (int i = 0; i < 3; i++) {
                    if (randPos[i] == toPos) {
                        Random rand = new Random();
                        int randCell = 81;
                        while (cell[randCell] != 0 || contains(randCell, randPos)) {
                            randCell = rand.nextInt(80);
                        }
                        cell[randCell] = -randBall[i];
                        randPos[i] = randCell;
                    }
                }
                
                growTimeline = 20;
                //NewTurn();
            }
        }
    }
    
    public void paint(Graphics g) {
        //ve ban co
        for (int i = 0; i < 81; i++) {
            if (cell[i] > 0 && cell[i] < 8) {
                ballSprite[cell[i]].setPosition(cellPosition[i][0], cellPosition[i][1]);
                if (i == selBall) { //bong dang duoc chon
                    ballSprite[cell[i]].setFrame(selTimeline);
                } else if (destroy.contains(Integer.toString(i)) && destroyTimeline >= 7 && destroyTimeline < 16) { //bong dang bi huy
                    ballSprite[cell[i]].setFrame(destroyTimeline);
                } else { //bong binh thuong
                    ballSprite[cell[i]].setFrame(0);
                }
                ballSprite[cell[i]].paint(g);
            } else if (cell[i] > -8 && cell[i] < 0) { //bong dang nho
                int ballType = -cell[i];
                ballSprite[ballType].setPosition(cellPosition[i][0], cellPosition[i][1]);
                if(growTimeline >= 18) {
                    ballSprite[ballType].setFrame(growTimeline);
                } else {
                    ballSprite[ballType].setFrame(21);
                }
                ballSprite[ballType].paint(g);
            } else { //khong co gi
                g.drawImage(emptyCellImage, cellPosition[i][0], cellPosition[i][1], Graphics.LEFT | Graphics.TOP);
            }
        }
        
        //bao truoc cac qua bong moi
        for(int i = 0; i < 3; i++) {
            nextSprite.setFrame(randBall[i]);
            nextSprite.setPosition(134 + i * 30, 76);
            nextSprite.paint(g);
        }
    }
    
    private void newTurn() {
        for (int i = 0; i < 3; i++) {
            cell[randPos[i]] = randBall[i];
        }

        //kiem tra xem bong moi no co an khong
        destroy = checkDestroy(randPos);
        if (destroy.size() > 0) {
            addScore(destroy.size());
            destroyTimeline = 7;
        }

        if (!checkEndGame()) {
            new3Ball();
        } else {
            //game over
            if(score > record) {
                try {
                    RecordStore rs = RecordStore.openRecordStore(Main.RMS, false);
                    byte[] writer = Integer.toString(score).getBytes();
                    rs.setRecord(1, writer, 0, writer.length);
                    rs.closeRecordStore();
                } catch (RecordStoreException ex) {
                }
                record = score;
                parent.updateRecord(record);
            }
            parent.gameOver();
        }
    }
    
    public VectorHelper checkDestroy(int[] posList) {
        VectorHelper destroy = new VectorHelper();
        for (int i = 0; i < 3; i++) {
            if (!destroy.contains(Integer.toString(posList[i]))) {
                destroy.addAll(checkDestroy(posList[i]));
            }
        }

        return destroy;
    }
    
    private void swapCell(int posA, int posB) {
        int curBall = cell[posA];
        cell[posA] = 0;
        for (int i = 0; i < 3; i++) {
            if (randPos[i] == posA) {
                cell[posA] = -randBall[i];
                break;
            }
        }
        cell[posB] = curBall;
    }
    
    private void new3Ball() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int randCell = 81;
            while (cell[randCell] != 0 || contains(randCell, randPos)) {
                randCell = rand.nextInt(80);
            }
            randPos[i] = randCell;
            randBall[i] = rand.nextInt(6) + 1;
            cell[randCell] = -randBall[i];
        }
    }
    
    private boolean contains(int value, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (value == array[i]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkCanMove(int fromPos, int toPos) {
        Map visitedList = new Map();
        //Map<Integer, Integer> visitedList = new HashMap<Integer, Integer>();
        open = new Vector();
        close = new Vector();
        open.addElement(Integer.toString(fromPos));

        while(open.size() > 0 && !open.contains(Integer.toString(toPos))) {
            //lay ra vi tri cuoi cung trong open va dua no vao close
            int curPos = Integer.parseInt(open.elementAt(0).toString());
            close.addElement(Integer.toString(curPos));
            open.removeElementAt(0);

            //tien hanh kiem tra
            int nextPos;
            int curCol = curPos % 9;

            //ben tren
            if (curPos > 8) {
                nextPos = curPos - 9;
                if (cell[nextPos] <= 0 && cell[nextPos] > -8 && !open.contains(Integer.toString(nextPos)) && !close.contains(Integer.toString(nextPos))) {
                    open.addElement(Integer.toString(nextPos));
                    visitedList.put(nextPos, curPos);
                }
            }

            //ben phai
            if (curCol < 8) {
                nextPos = curPos + 1;
                if (cell[nextPos] <= 0 && cell[nextPos] > -8 && !open.contains(Integer.toString(nextPos)) && !close.contains(Integer.toString(nextPos))) {
                    open.addElement(Integer.toString(nextPos));
                    visitedList.put(nextPos, curPos);
                }
            }

            //ben duoi
            if (curPos < 72) {
                nextPos = curPos + 9;
                if (cell[nextPos] <= 0 && cell[nextPos] > -8 && !open.contains(Integer.toString(nextPos)) && !close.contains(Integer.toString(nextPos))) {
                    open.addElement(Integer.toString(nextPos));
                    visitedList.put(nextPos, curPos);
                }
            }

            //ben trai
            if (curCol > 0) {
                nextPos = curPos - 1;
                if (cell[nextPos] <= 0 && cell[nextPos] > -8 && !open.contains(Integer.toString(nextPos)) && !close.contains(Integer.toString(nextPos))) {
                    open.addElement(Integer.toString(nextPos));
                    visitedList.put(nextPos, curPos);
                }
            }
        }

        if (close.contains(Integer.toString(toPos)) || open.contains(Integer.toString(toPos))) {
            trackPath = new VectorHelper();
            trackPath.addElement(Integer.toString(toPos));
            int curPos = toPos;
            while (curPos != fromPos) {
                trackPath.addElement(Integer.toString(visitedList.get(curPos)));
                curPos = visitedList.get(curPos);
            }
            trackPath.addElement(Integer.toString(fromPos));
            trackPath.reverse();
            
            return true;
        }

        return false;
    }
    
    private boolean checkEndGame() {
        int totalBall = 0;
        for (int i = 0; i < 81; i++) {
            if (cell[i] > 0) {
                totalBall++;
            }
        }
        if (totalBall >= 78) {
            return true;
        }
        return false;
    }
    
    private void createStepback() {
        stepbackCell = new int[82];
        System.arraycopy(cell, 0, stepbackCell, 0, 82);
        stepbackRandBall = new int[3];
        System.arraycopy(randBall, 0, stepbackRandBall, 0, 3);
        stepbackRandPos = new int[3];
        System.arraycopy(randPos, 0, stepbackRandPos, 0, 3);
        stepbackScore = score;
        haveStepback = true;
    }
    
    public void doStepback() {
        if(haveStepback) {
            cell = stepbackCell;
            randPos = stepbackRandPos;
            randBall = stepbackRandBall;
            score = stepbackScore;
            parent.updateScore(score);
            haveStepback = false;
            selBall = -1;
        }
    }
    
    public boolean haveStepback() {
        return haveStepback;
    }
    
    public void click(int x, int y) {
        if (boardArea.contains(x, y) && trackPath.isEmpty()) {
            x -= boardArea.x;
            y -= boardArea.y;
            int col = x / 26;
            int row = y / 26;
            selectBall(row * 9 + col);
        }
    }
    
    private void selectBall(int cellIndex) {
        if (cell[cellIndex] > 0) { //truong hop o bam ton tai bong
            if (selBall != cellIndex) {
                selBall = cellIndex;
            }
        } else if (selBall != -1) {	//truong hop o bam khong ton tai bong va co bong dang duoc chon
            if (checkCanMove(selBall, cellIndex)) {
                createStepback();
                selBall = -1;
            } else {
                //neu khong di chuyen duoc
                System.out.println("Can't move!");
            }
        }
    }
    
    public void newBoard() {
        Random rand = new Random();

        //tao 5 bong dau tien
        for (int i = 0; i < 5; i++) {
            int cellIndex = 81;
            while (cell[cellIndex] != 0) {
                cellIndex = rand.nextInt(80);
            }
            int randBall = rand.nextInt(6) + 1;
            cell[cellIndex] = randBall;
        }

        //tao 3 bong moi ngau nhien
        new3Ball();
    }
    
    public void clearBoard() {
        for(int i = 0; i < 81; i++) {
            cell[i] = 0;
        }
        cell[81] = 8;
        score = 0;
        haveStepback = false;
    }
    
    public int getScore() { return score; }
}
