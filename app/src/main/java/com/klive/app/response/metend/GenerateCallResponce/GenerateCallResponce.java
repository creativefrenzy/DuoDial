package com.klive.app.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCallResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private GenerateCallResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public GenerateCallResult getResult() {
        return result;
    }

    public void setResult(GenerateCallResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
