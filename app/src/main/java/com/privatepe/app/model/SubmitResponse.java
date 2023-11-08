package com.privatepe.app.model;

import com.google.gson.annotations.SerializedName;

public class SubmitResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("result")
    private String result;

    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
