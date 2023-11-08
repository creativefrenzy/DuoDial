package com.privatepe.app.response.NewVideoStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewVideoStatusResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<NewVideoStatusResult> result;

    @SerializedName("error")
    @Expose
    private String error;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<NewVideoStatusResult> getResult() {
        return result;
    }

    public void setResult(List<NewVideoStatusResult> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
