package net;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */

public class TimeUtils {

    //字符串转时间戳
    public static String getTime(String timeString){
        String timeStamp = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(Exception e){
            e.printStackTrace();
        }
        return timeStamp;
    }


    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        return getStrTime(timeStamp, "yyyy-MM-dd HH:mm:ss");
    }


    public static String getStrTime(String timeStamp, String dateFormate) {
        String timeString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }
}
