package com.privatepe.host.response.AgencyHostWeekly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgencyHostWeeklyResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private AgencyHostWeeklyResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public AgencyHostWeeklyResult getResult() {
        return result;
    }

    public void setResult(AgencyHostWeeklyResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}