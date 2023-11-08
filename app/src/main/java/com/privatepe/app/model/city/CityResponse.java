package com.privatepe.app.model.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("result")
    @Expose
    private List<CityResult> getResponse = null;
    @SerializedName("error")
    String error;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public List<CityResult> getGetResponse() {
        return getResponse;
    }
}
