package com.privatepe.app.response.metend.store.response.use_or_remove;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UseOrRemoveItemResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
