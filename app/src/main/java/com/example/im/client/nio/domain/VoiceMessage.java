package com.example.im.client.nio.domain;

/**
 * Created by ganchenqing on 2018/5/8.
 */

public class VoiceMessage extends Message {
    private static final long serialVersionUID = -6849123470754667710L;

    private int recorderLength;

    private byte[] voiceData;

    private String voiceName;

    private String voiceUrl;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getRecorderLength() {
        return recorderLength;
    }

    public void setRecorderLength(int recorderLength) {
        this.recorderLength = recorderLength;
    }

    public byte[] getVoiceData() {
        return voiceData;
    }

    public void setVoiceData(byte[] voiceData) {
        this.voiceData = voiceData;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    @Override
    public String getId() {
        return getFrom();
    }
}
