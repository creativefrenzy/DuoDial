package com.privatepe.host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Login {
    @SerializedName("success")
    private boolean success;

    @SerializedName("already_registered")
    private String already_registered;

    @SerializedName("result")
    @Expose
    private GetData getResponse = null;

    public GetData getInfo() {
        return getResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public String isAlready_registered() {
        return already_registered;
    }
}
