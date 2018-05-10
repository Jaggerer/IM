package com.example.im.client.nio;


import com.example.im.client.ConnectionService;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.MessageType;
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.domain.StringMessage;
import com.example.im.client.nio.domain.VoiceMessage;
import com.example.im.db.bean.MyMessage;
import com.orhanobut.logger.Logger;

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
            MyMessage myMessage = new MyMessage();
            PicMessage picMessage = (PicMessage) message;

            Logger.d("收到图片的URL为--> " + (picMessage.getPicUrl()));

            myMessage.putPicMessage(picMessage);
            mBinder.receiveMessage(myMessage);

        } else if (messageType == MessageType.stringMessage) {
            Logger.d("收到消息,类型为: 文字");
            MyMessage myMessage = new MyMessage();
            StringMessage stringMessage = (StringMessage) message;
            myMessage.putStringMessage(stringMessage);
            mBinder.receiveMessage(myMessage);
        } else if (messageType == MessageType.voiceMessage) {
            MyMessage myMessage = new MyMessage();
            VoiceMessage voiceMessage = (VoiceMessage) message;
            myMessage.putVoiceMessage(voiceMessage);
            mBinder.receiveMessage(myMessage);
            Logger.d("收到消息,类型为: 语音");
        }
    }

}
