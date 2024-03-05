package com.privatepe.host.response.SettlementDate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SettlementHostWeeklyResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private SettlementHostWeeklyResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SettlementHostWeeklyResult getResult() {
        return result;
    }

    public void setResult(SettlementHostWeeklyResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}