package com.privatepe.app.response.temporary_block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempBlockResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private TemporaryBlockResult result;
    @SerializedName("error")
    @Expose
    private Object error;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public TemporaryBlockResult getResult() {
        return result;
    }

    public void setResult(TemporaryBlockResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
