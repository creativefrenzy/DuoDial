package com.privatepe.host.response.Auto_Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoMessageRequest {
    @SerializedName("user_id")
    @Expose
    private List<Integer> userId;

   /* public AutoMessageRequest(List<Integer> userId) {
        this.userId = userId;
    }*/

    public List<Integer> getUserId() {
        return userId;
    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }
}