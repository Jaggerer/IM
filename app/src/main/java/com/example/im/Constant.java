package com.example.im;

import android.os.Environment;

import java.io.File;

/**
 * Created by ganchenqing on 2018/3/20.
 */

public class Constant {
    //文本
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_RECEIVER_TXT = 3;
    public static final int TYPE_SEND_TXT = 4;
    //图片
    public static final int TYPE_PIC = 1;
    public static final int TYPE_SEND_IMAGE = 5;
    public static final int TYPE_RECEIVER_IMAGE = 6;
    //语音
    public static final int TYPE_VOICE = 2;
    public static final int TYPE_SEND_VOICE = 7;
    public static final int TYPE_RECEIVER_VOICE = 8;
//    //视频
//    public static final int TYPE_SEND_VIDEO = 6;
//    public static final int TYPE_RECEIVER_VIDEO = 7;

    public static int REQUEST_CODE_SELECT_IMG = 1;

    public static int SEND_SUC = 1000;
    public static int SENDING = 1001;
    public static int SEND_FAILED = 1002;

    public static final int REQUEST_CAMERA = 28;
    public static final int REQUEST_MIC = 29;




}
