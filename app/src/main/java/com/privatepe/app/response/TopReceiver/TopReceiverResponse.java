package com.privatepe.app.response.TopReceiver;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopReceiverResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private List<TopReceiverResult> topReceiverResult = null;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TopReceiverResult> getResult() {
        return topReceiverResult;
    }

    public void setResult(List<TopReceiverResult> topReceiverResult) {
        this.topReceiverResult = topReceiverResult;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}

