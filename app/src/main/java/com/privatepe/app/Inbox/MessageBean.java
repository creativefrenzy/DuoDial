package com.privatepe.app.Inbox;

import java.io.Serializable;


public class MessageBean implements Serializable {
    private String account;
    private Messages message;
    private int background;
    private boolean beSelf;
    private String timestamp;
    private String msgDate;
    private boolean isPlayingAudio=false;

    public MessageBean(String account, Messages message, boolean beSelf, String timestamp) {
        this.account = account;
        this.message = message;
        this.beSelf = beSelf;
        this.timestamp = timestamp;
    }

    public MessageBean() {

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public boolean isBeSelf() {
        return beSelf;
    }

    public void setBeSelf(boolean beSelf) {
        this.beSelf = beSelf;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public boolean isPlayingAudio() {
        return isPlayingAudio;
    }

    public void setPlayingAudio(boolean playingAudio) {
        isPlayingAudio = playingAudio;
    }
}
