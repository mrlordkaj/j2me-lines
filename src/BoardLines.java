/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import util.VectorHelper;

/**
 *
 * @author Thinh
 */
public class BoardLines extends Board {
    
    public BoardLines(Game _parent) {
        super(_parent);
    }

    public VectorHelper checkDestroy(int pos) {
        int ballType = cell[pos];
        int k;
        VectorHelper tempDestroy;
        VectorHelper destroy = new VectorHelper();

        //theo chieu doc
        k = pos;
        tempDestroy = new VectorHelper();
        while (k > 8) {
            k -= 9;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        k = pos;
        while (k < 72) {
            k += 9;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        if (tempDestroy.size() >= 4) {
            destroy.addAll(tempDestroy);
        }

        //theo chieu ngang
        tempDestroy = new VectorHelper();
        k = pos;
        while (k > 0 && (k % 9) != 0) {
            k -= 1;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        k = pos;
        while (k < 80 && (k % 9) != 8) {
            k += 1;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        if (tempDestroy.size() >= 4) {
            destroy.addAll(tempDestroy);
        }

        //theo duong cheo nguoc
        tempDestroy = new VectorHelper();
        k = pos;
        while (k >= 8 && (k % 9) != 8) {
            k -= 8;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        k = pos;
        while (k <= 72 && (k % 9) != 0) {
            k += 8;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        if (tempDestroy.size() >= 4) {
            destroy.addAll(tempDestroy);
        }

        //theo duong cheo xuoi
        tempDestroy = new VectorHelper();
        k = pos;
        while (k > 9 && (k % 9) != 0) {
            k -= 10;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        k = pos;
        while (k < 71 && (k % 9) != 8) {
            k += 10;
            if (cell[k] == ballType) {
                tempDestroy.addElement(Integer.toString(k));
            } else {
                break;
            }
        }
        if (tempDestroy.size() >= 4) {
            destroy.addAll(tempDestroy);
        }

        if (destroy.size() > 0) {
            destroy.addElement(Integer.toString(pos));
        }

        return destroy;
    }

    public void addScore(int numBall) {
        score += numBall * 2 - 5;
        parent.updateScore(score);
    }
}
