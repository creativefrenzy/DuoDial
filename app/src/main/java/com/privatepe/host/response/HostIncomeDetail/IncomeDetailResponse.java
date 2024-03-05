package com.privatepe.host.response.HostIncomeDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeDetailResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private IncomeDetailResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public IncomeDetailResult getResult() {
        return result;
    }

    public void setResult(IncomeDetailResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}