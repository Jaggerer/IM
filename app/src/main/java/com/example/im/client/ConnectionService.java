package com.example.im.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.im.client.nio.ClientMessageService;
import com.example.im.client.nio.MessageService;
import com.example.im.client.nio.NioClient;
import com.example.im.client.nio.domain.IdMessage;
import com.example.im.utils.SPUtil;

import java.io.IOException;

/**
 * Created by ganchenqing on 2018/4/16.
 */

public class ConnectionService extends Service {
    private ClientBinder mBinder = new ClientBinder();
    private SPUtil spUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        spUtil = new SPUtil(this);
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
        NioClient client;

        public void connectService(){
            MessageService messageService = new ClientMessageService();
            client = new NioClient(messageService);
            //测试发消息
            new Thread(() -> {
                IdMessage idMessage = new IdMessage();
                String userName = spUtil.getString("username", "");
                idMessage.setFrom(userName);
                messageService.addMessage(idMessage);
                try {
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
