package com.example.im.event;

import com.example.im.db.bean.MyMessage;

/**
 * Created by ganchenqing on 2018/3/28.
 */

public class AddMessageEvent {
    private MyMessage myMessage;

    public AddMessageEvent(MyMessage myMessage) {
        this.myMessage = myMessage;
    }

    public MyMessage getMyMessage() {
        return myMessage;
    }
}
