package com.example.im;

import android.os.Environment;

import java.io.File;

/**
 * Created by ganchenqing on 2018/3/20.
 */

public class Constant {
    //文本
    public static final int TYPE_TEXT = 5;
    public static final int TYPE_RECEIVER_TXT = 3;
    public static final int TYPE_SEND_TXT = 4;
    //图片
    public static final int TYPE_PIC = 10;
    public static final int TYPE_SEND_IMAGE = 5;
    public static final int TYPE_RECEIVER_IMAGE = 6;
    //语音
    public static final int TYPE_VOICE = 20;
    public static final int TYPE_SEND_VOICE = 7;
    public static final int TYPE_RECEIVER_VOICE = 8;
    //中间数据
    public static final int TYPE_CONTENT = 50;
    //结束数据
    public static final int TYPE_ENDING = 100;

//    //视频
//    public static final int TYPE_SEND_VIDEO = 6;
//    public static final int TYPE_RECEIVER_VIDEO = 7;

    public static int REQUEST_CODE_SELECT_IMG = 1;

    public static int SEND_SUC = 1000;
    public static int SENDING = 1001;
    public static int SEND_FAILED = 1002;

    public static final int REQUEST_CAMERA = 28;
    public static final int REQUEST_MIC = 29;


    //当前用户的ID
    public static final String CURRENT_USERID = "1";
    //发送消息用户的ID
    public static final String OTHER_USERID = "2";

    public static final int REQUEST_SUC = 1;

    public static final String HOST_URL = "10.0.2.2";
    public static final String HTTP_PORT = "8080";
    public static final String NIO_PORT = "8888";
    public static final String HTTP_HOST_URL = "http://" + HOST_URL + ":" + HTTP_PORT;


}
