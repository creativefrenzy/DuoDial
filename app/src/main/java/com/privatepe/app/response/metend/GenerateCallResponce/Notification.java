package com.privatepe.app.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("connecting_profile_id")
    @Expose
    private String connectingProfileId;
    @SerializedName("connecting_id")
    @Expose
    private Integer connectingId;
    @SerializedName("caller_id")
    @Expose
    private Integer callerId;
    @SerializedName("call_time")
    @Expose
    private String callTime;

    public String getConnectingProfileId() {
        return connectingProfileId;
    }

    public void setConnectingProfileId(String connectingProfileId) {
        this.connectingProfileId = connectingProfileId;
    }

    public Integer getConnectingId() {
        return connectingId;
    }

    public void setConnectingId(Integer connectingId) {
        this.connectingId = connectingId;
    }

    public Integer getCallerId() {
        return callerId;
    }

    public void setCallerId(Integer callerId) {
        this.callerId = callerId;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

}
