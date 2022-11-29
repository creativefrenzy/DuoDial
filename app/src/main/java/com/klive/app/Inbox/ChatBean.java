package com.klive.app.Inbox;

public class ChatBean {
    private String id;
    private String chat_receive;
    private String receive_time;
    private String chat_sent;
    private String sent_time;
    private String date;
    private String chatType;
    public ChatBean(String id, String chat_receive, String receive_time, String chat_sent, String sent_time,String chatType) {
        this.id = id;
        this.chat_receive = chat_receive;
        this.receive_time = receive_time;
        this.chat_sent = chat_sent;
        this.sent_time = sent_time;
        this.chatType=chatType;
    }
/*  public ChatBean(String id, String chat_receive, String receive_time, String chat_sent, String sent_time, String date,String chatType) {
        this.id = id;
        this.chat_receive = chat_receive;
        this.receive_time = receive_time;
        this.chat_sent = chat_sent;
        this.sent_time = sent_time;
        this.date = date;
        this.chatType=chatType;
    }*/
    public ChatBean() {

    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getChat_receive() {
        return chat_receive;
    }

    public String getReceive_time(){
        return receive_time;
    }

    public String getChat_sent(){
        return chat_sent;
    }

    public String getSent_time(){
        return sent_time;
    }


    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
