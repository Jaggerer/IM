package com.example.im;

/**
 * Created by ganchenqing on 2018/3/19.
 */

public class Message {
    public static final int TYPE_STRING = 0;
    public static final int TYPE_PIC = 1;
    public static final int TYPE_VOICE = 2;
    public static final int TYPE_VEDIO = 3;

    private String fromId;
    private String toId;

    private int messageType;
    private String content;
    private String fileDir;

    private long createTime;

    private int sendStatus;
    //    录音情况下的录音长度
    private int recorderLength;
    //    业务类型
    private String remarkId;


    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getRecorderLength() {
        return recorderLength;
    }

    public void setRecorderLength(int recorderLength) {
        this.recorderLength = recorderLength;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }
}
