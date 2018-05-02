package com.example.im;

import android.app.Application;

import com.example.im.utils.imageloaderutil.UniversalImageLoader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;

/**
 * Created by ganchenqing on 2018/3/21.
 */

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UniversalImageLoader.initImageLoader(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Realm.init(this);
    }
}
