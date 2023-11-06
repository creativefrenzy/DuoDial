package com.klive.app.response.metend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.klive.app.response.metend.RechargePlan.RechargePlanResponseNew;

public class FirstTimeRechargeListResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private RechargePlanResponseNew.Data result;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public RechargePlanResponseNew.Data getResult() {
        return result;
    }

    public void setResult(RechargePlanResponseNew.Data result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
