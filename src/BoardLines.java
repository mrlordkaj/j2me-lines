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

import util.VectorHelper;

/**
 *
 * @author Thinh Pham
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
