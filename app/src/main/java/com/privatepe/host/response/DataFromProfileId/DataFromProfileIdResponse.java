package com.privatepe.host.response.DataFromProfileId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFromProfileIdResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private DataFromProfileIdResult result;

    @SerializedName("error")
    @Expose
    private String error;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public DataFromProfileIdResult getResult() {
        return result;
    }

    public void setResult(DataFromProfileIdResult result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
