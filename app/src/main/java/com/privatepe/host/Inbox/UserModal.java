package com.privatepe.host.Inbox;

public class UserModal {
    private String peer_id;
    private String peer_name;
    private String peer_last_msg;
    private String last_time;
    private String peer_icon;

    public void setPeer_id(String peer_id) {
        this.peer_id = peer_id;
    }

    public void setPeer_name(String peer_name) {
        this.peer_name = peer_name;
    }

    public void setPeer_last_msg(String peer_last_msg) {
        this.peer_last_msg = peer_last_msg;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public void setPeer_icon(String peer_icon) {
        this.peer_icon = peer_icon;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

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
