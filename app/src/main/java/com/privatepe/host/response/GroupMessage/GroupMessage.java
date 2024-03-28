package com.privatepe.host.response.GroupMessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupMessage {
    public String type;
    public String message;
    public String from;
    public String fromName;
    public String fromImage;
    public String timeStamp;

    public GroupMessage(String type, String message, String from, String fromName, String fromImage, String timeStamp) {
        this.type = type;
        this.message = message;
        this.from = from;
        this.fromName = fromName;
        this.fromImage = fromImage;
        this.timeStamp = timeStamp;
    }
}
