package com.example.im;

import android.app.Application;

import com.example.im.utils.imageloaderutil.UniversalImageLoader;

/**
 * Created by ganchenqing on 2018/3/21.
 */

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UniversalImageLoader.initImageLoader(this);
    }
}
