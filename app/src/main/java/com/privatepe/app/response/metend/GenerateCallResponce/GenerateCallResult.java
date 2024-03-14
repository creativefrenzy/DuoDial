package com.privatepe.app.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCallResult {
    @SerializedName("unique_id")
    @Expose
    private String unique_id;
    @SerializedName("total_point")
    @Expose
    private long total_point;

    @SerializedName("receiver_device_token")
    @Expose
    private String receiver_device_token;

    public String getReceiver_device_token() {
        return receiver_device_token;
    }

    public void setReceiver_device_token(String receiver_device_token) {
        this.receiver_device_token = receiver_device_token;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public Long getPoints() {
        return total_point;
    }

    public void setPoints(Long points) {
        this.total_point = points;
    }
}
