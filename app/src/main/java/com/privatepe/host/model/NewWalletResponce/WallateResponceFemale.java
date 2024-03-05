package com.privatepe.host.model.NewWalletResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WallateResponceFemale {
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @SerializedName("result")
    @Expose
    private WalletResponceFemaleData result;
    @SerializedName("error")
    @Expose
    private Object error;

    public WalletResponceFemaleData getResult() {
        return result;
    }

    public void setResult(WalletResponceFemaleData result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
