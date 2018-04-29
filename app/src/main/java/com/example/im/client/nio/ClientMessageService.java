package com.example.im.client.nio;


import android.util.Log;

import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.MessageType;
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.domain.StringMessage;
import com.orhanobut.logger.Logger;

public class ClientMessageService extends MessageService {


    @Override
    protected void _doMessage(Message message) {
        //todo 业务逻辑
        int messageType = MessageType.getMessageType(message);
        if (messageType == MessageType.picMessage) {
            Logger.d("收到消息,类型为: 图片");
            Logger.d(((PicMessage) message).getPicUrl());
        } else if (messageType == MessageType.stringMessage) {
            Logger.d("收到消息,类型为: 图片");
            Logger.d(((StringMessage) message).getContent());
        } else if (messageType == MessageType.amrMessage) {
            Logger.d("收到消息,类型为: 语音");
        }
    }

}
