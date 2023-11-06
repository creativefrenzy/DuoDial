package com.klive.app.response.metend.upi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetActiveUPIResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private ActiveUpiResult result;

    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ActiveUpiResult getResult() {
        return result;
    }

    public void setResult(ActiveUpiResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }


}
