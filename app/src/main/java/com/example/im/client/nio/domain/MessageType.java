package com.example.im.client.nio.domain;

public class MessageType {

    public static final byte picMessage = 1;

    public static final byte voiceMessage = 2;

    public static final byte stringMessage = 3;

    public static final byte idMessage = 4;


    public static byte getMessageType(Message message) {
        byte type = 0;
        if (message instanceof StringMessage) {
            type = stringMessage;
        } else if (message instanceof VoiceMessage) {
            type = voiceMessage;
        } else if (message instanceof PicMessage) {
            type = picMessage;
        } else if (message instanceof IdMessage) {
            type = idMessage;
        }
        return type;

    }

}
