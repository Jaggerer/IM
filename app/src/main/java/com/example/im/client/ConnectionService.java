package com.example.im.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ganchenqing on 2018/4/16.
 */

public class ConnectionService extends Service {
    private ClientBinder mBinder = new ClientBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tag", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("tag", "onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("tag", "onRebind");
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("tag", "onBind");
        return mBinder;
    }

    public class ClientBinder extends Binder {

        public void connectService() {
            Log.d("tag", "connectService");
        }

        public void sendTextMessage(){
            Log.d("tag", "sendTextMessage");
        }
    }
}
