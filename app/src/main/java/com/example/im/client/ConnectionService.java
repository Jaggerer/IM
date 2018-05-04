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
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.domain.StringMessage;
import com.example.im.db.bean.ChatRecordBean;
import com.example.im.db.bean.MyMessage;
import com.example.im.db.bean.UserBean;
import com.example.im.event.RefreshEvent;
import com.example.im.utils.URIUtils;
import com.example.im.utils.UserUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
        NioClient client;

        public void connectService() {
            MessageService messageService = new ClientMessageService(this);
            client = new NioClient(messageService);
            new Thread(() -> {
                IdMessage idMessage = new IdMessage();
                String userName = UserUtils.getCurrentUser(ConnectionService.this);
                idMessage.setFrom(userName);
                messageService.addMessage(idMessage);
                try {
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        public void sendMessage(MyMessage message) {
            switch (message.getMessageType()) {
                case MyMessage.TYPE_PIC:
                    PicMessage picMessage = new PicMessage();
                    File picFile = new File(message.getFileDir());
                    picMessage.setPicName(picFile.getName());
                    picMessage.setCreatedTime(message.getCreateTime());
                    picMessage.setFrom(message.getFromId());
                    picMessage.setTo(message.getToId());
                    picMessage.setPicture(URIUtils.readFile(picFile));
                    client.addMessage(picMessage);
                    break;
                case MyMessage.TYPE_VOICE:
                    break;
                case MyMessage.TYPE_STRING:
                    StringMessage stringMessage = new StringMessage();
                    stringMessage.setCreatedTime(message.getCreateTime());
                    stringMessage.setFrom(message.getFromId());
                    stringMessage.setTo(message.getToId());
                    stringMessage.setContent(message.getContent());
                    client.addMessage(stringMessage);
                    break;
            }

        }

        public ConnectionService getService() {
            return ConnectionService.this;
        }


        public void receiveMessage(MyMessage myMessage) {
            Realm mRealm = Realm.getDefaultInstance();
            String userName = UserUtils.getCurrentUser(ConnectionService.this);

            RealmResults<UserBean> userList = mRealm.where(UserBean.class)
                    .equalTo("currentUserName", userName).findAll();

            if (userList.size() == 0) {
                mRealm.beginTransaction();
                UserBean userBean = mRealm.createObject(UserBean.class);
                userBean.setCurrentUserName(myMessage.getFromId());
                userBean.getRecentUserName().add(myMessage.getToId());
                mRealm.commitTransaction();
            } else {
                UserBean userBean = userList.get(0);
                List<String> recentList = userBean.getRecentUserName();
                if (!recentList.contains(myMessage.getToId())) {
                    mRealm.beginTransaction();
                    recentList.add(myMessage.getToId());
                    mRealm.commitTransaction();
                }
            }


            RealmResults<ChatRecordBean> recordList = mRealm.where(ChatRecordBean.class)
                    .contains("userName", myMessage.getFromId()).and()
                    .contains("userName", myMessage.getToId())
                    .findAll();
            if (recordList.size() == 0) {
                mRealm.beginTransaction();
                ChatRecordBean chatRecordBean = mRealm.createObject(ChatRecordBean.class);
                chatRecordBean.setUserName(myMessage.getFromId() + "_" + myMessage.getToId());
                mRealm.copyToRealm(myMessage);
                chatRecordBean.getMessageList().add(myMessage);
                mRealm.commitTransaction();
            } else {
                ChatRecordBean chatRecordBean = recordList.get(0);
                RealmList<MyMessage> mList = chatRecordBean.getMessageList();
                mRealm.beginTransaction();
                mRealm.copyToRealm(myMessage);
                mList.add(myMessage);
                mRealm.commitTransaction();
            }
            EventBus.getDefault().post(new RefreshEvent());
        }
    }
}
