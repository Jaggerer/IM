package com.example.im.client.nio;


import com.example.im.client.ConnectionService;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.MessageType;
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.domain.StringMessage;
import com.example.im.db.bean.MyMessage;
import com.example.im.event.RefreshEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class ClientMessageService extends MessageService {
    ConnectionService.ClientBinder mBinder;

    public ClientMessageService(ConnectionService.ClientBinder clientBinder) {
        mBinder = clientBinder;
    }

    @Override
    protected void _doMessage(Message message) {
        //todo 业务逻辑
        int messageType = MessageType.getMessageType(message);
        if (messageType == MessageType.picMessage) {
            Logger.d("收到消息,类型为: 图片");
            Logger.d(((PicMessage) message).getPicUrl());
        } else if (messageType == MessageType.stringMessage) {
            Logger.d("收到消息,类型为: 文字");
            MyMessage myMessage = new MyMessage();
            StringMessage stringMessage = (StringMessage) message;
            myMessage.putStringMessage(stringMessage);
            mBinder.receiveMessage(myMessage);
            EventBus.getDefault().post(new RefreshEvent());
        } else if (messageType == MessageType.amrMessage) {
            Logger.d("收到消息,类型为: 语音");
        }
    }

}
