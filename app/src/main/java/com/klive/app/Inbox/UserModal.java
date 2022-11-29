package com.klive.app.Inbox;

public class UserModal {
    private String peer_id;
    private String peer_name;
    private String peer_last_msg;
    private String last_time;
    private String peer_icon;
    private Integer count;
    private String chatType;
    public UserModal(String peer_id, String peer_name, String peer_last_msg, String last_time, String peer_icon, Integer count,String chatType) {

        this.peer_id = peer_id;
        this.peer_name = peer_name;
        this.peer_last_msg = peer_last_msg;
        this.last_time = last_time;
        this.peer_icon = peer_icon;
        this.count = count;
        this.chatType=chatType;
    }

    public Integer getCount() {
        return count;
    }

    public String getPeer_icon(){
        return peer_icon;
    }

    public String getPeer_id() {
        return peer_id;
    }

    public String getPeer_name(){
        return peer_name;
    }

    public String getPeer_last_msg(){
        return peer_last_msg;
    }

    public String getLast_time(){
        return last_time;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
