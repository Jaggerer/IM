package com.example.im.db.bean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by ganchenqing on 2018/4/30.
 */

public class ChatRecordBean extends RealmObject {
    public String userName;
    public RealmList<MyMessage> messageList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RealmList<MyMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(RealmList<MyMessage> messageList) {
        this.messageList = messageList;
    }
}
