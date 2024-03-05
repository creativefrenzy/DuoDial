package com.privatepe.host.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCallData {
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("sender_channel_name")
    @Expose
    private GenerateCallChannelName senderChannelName;
    @SerializedName("receiver_channel_name")
    @Expose
    private GenerateCallChannelName receiverChannelName;
    @SerializedName("notification")
    @Expose
    private Notification notification;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public GenerateCallChannelName getSenderChannelName() {
        return senderChannelName;
    }

    public void setSenderChannelName(GenerateCallChannelName senderChannelName) {
        this.senderChannelName = senderChannelName;
    }

    public GenerateCallChannelName getReceiverChannelName() {
        return receiverChannelName;
    }

    public void setReceiverChannelName(GenerateCallChannelName receiverChannelName) {
        this.receiverChannelName = receiverChannelName;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
