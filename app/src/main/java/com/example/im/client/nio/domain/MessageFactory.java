package com.example.im.client.nio.domain;

public class MessageFactory {
    private static final long serialVersionUID = -6849123470754667710L;

    public static Class getMessageClass(int type) {
        Class clazz = null;
        switch (type) {
            case MessageType.stringMessage:
                clazz = StringMessage.class;
                break;
            case MessageType.picMessage:
                clazz = PicMessage.class;
                break;
            case MessageType.voiceMessage:
                clazz = VoiceMessage.class;
                break;
            case MessageType.idMessage:
                clazz = IdMessage.class;
                break;

        }
        return clazz;
    }


}
