package com.privatepe.app.extras;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatepe.app.response.Banner.BannerResult;

import java.util.List;

public class BannerResponseNew {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private List<BannerResult> result = null;
    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<BannerResult> getResult() {
        return result;
    }

    public void setResult(List<BannerResult> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}