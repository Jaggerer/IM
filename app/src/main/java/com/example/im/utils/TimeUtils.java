package com.example.im.utils;

import java.text.SimpleDateFormat;

/**
 * Created by ganchenqing on 2018/3/20.
 */

public class TimeUtils {
    public static String formatTime(long time,String formator){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formator);
        return dateFormat.format(time);
    }
}
