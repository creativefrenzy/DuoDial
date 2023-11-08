package com.privatepe.app.response.metend.new_notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewNotificationResult {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_id")
    @Expose
    private int profile_id;

    @SerializedName("messagedetails")
    @Expose
    private List<NotificationMsgDetail> notificationMsgDetail;

    @SerializedName("message_duration")
    @Expose
    private long messageDuration;

    @SerializedName("profile_images")
    @Expose
    private List<NotiProfileImages> notiProfileImages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public List<NotificationMsgDetail> getNotificationMsgDetail() {
        return notificationMsgDetail;
    }

    public void setNotificationMsgDetail(List<NotificationMsgDetail> notificationMsgDetail) {
        this.notificationMsgDetail = notificationMsgDetail;
    }

    public long getMessageDuration() {
        return messageDuration;
    }

    public void setMessageDuration(long messageDuration) {
        this.messageDuration = messageDuration;
    }

    public List<NotiProfileImages> getNotiProfileImages() {
        return notiProfileImages;
    }

    public void setNotiProfileImages(List<NotiProfileImages> notiProfileImages) {
        this.notiProfileImages = notiProfileImages;
    }


}
