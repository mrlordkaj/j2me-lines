/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Thinh
 */
public class StringHelper {
    public static String parseTime(int sec) {
        StringBuffer rs = new StringBuffer();
        int hour = sec / 3600;
        int minute = (sec - hour*3600) / 60;
        int second = sec % 60;
        if(hour < 10) rs.append("0");
        rs.append(hour + ":");
        if(minute < 10) rs.append("0");
        rs.append(minute + ":");
        if(second < 10) rs.append("0");
        rs.append(second);
        return rs.toString();
    }
}
