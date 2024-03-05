package com.privatepe.host.Inbox;

public class UserInfo {

    String id, user_id, user_name, time, user_photo, message, unread_msg_count, profile_id, msg_type, gift_count;

    public UserInfo() {
    }

    public UserInfo(String id, String user_id, String user_name, String message, String time, String user_photo, String unread_msg_count, String profile_id, String msg_type, String gift_count) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.message = message;
        this.time = time;
        this.user_photo = user_photo;
        this.unread_msg_count = unread_msg_count;
        this.profile_id = profile_id;
        this.msg_type = msg_type;
        this.gift_count = gift_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnread_msg_count() {
        return unread_msg_count;
    }

    public void setUnread_msg_count(String unread_msg_count) {
        this.unread_msg_count = unread_msg_count;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }
}