package com.example.im.event;

import com.example.im.chat.Message;

/**
 * Created by ganchenqing on 2018/3/28.
 */

public class AddMessageEvent {
    private Message message;

    public AddMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
