package com.example.im.client.nio.domain;

public class AmrMessage extends Message {
    private static final long serialVersionUID = -6849123470754667710L;

    private int recorderLength;

    private byte[] arm;

    private String armUrl;

    private String armName;

    public int getRecorderLength() {
        return recorderLength;
    }

    public void setRecorderLength(int recorderLength) {
        this.recorderLength = recorderLength;
    }

    public byte[] getArm() {
        return arm;
    }

    public void setArm(byte[] arm) {
        this.arm = arm;
    }

    @Override
    public String getId() {
        return super.getFrom();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getArmUrl() {
        return armUrl;
    }

    public void setArmUrl(String armUrl) {
        this.armUrl = armUrl;
    }

    public String getArmName() {
        return armName;
    }

    public void setArmName(String armName) {
        this.armName = armName;
    }
}
