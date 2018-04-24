package com.example.im.client.nio.domain;

public class MessageFactory {

    public static Class getMessageClass(int type){
        Class clazz=null;
        switch (type){
            case MessageType.stringMessage:
                clazz= StringMessage.class;
                break;
                case MessageType.picMessage:
                    clazz= PicMessage.class;
                    break;
                case MessageType.amrMessage:
                    clazz=AmrMessage.class;
                     break;

        }
        return clazz;
    }



}
