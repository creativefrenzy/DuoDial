package com.klive.app.model.BankList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private BankListResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BankListResult getResult() {
        return result;
    }

    public void setResult(BankListResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
