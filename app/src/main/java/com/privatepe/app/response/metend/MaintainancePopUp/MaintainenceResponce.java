package com.privatepe.app.response.metend.MaintainancePopUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaintainenceResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private MaintainenceData result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public MaintainenceData getResult() {
        return result;
    }

    public void setResult(MaintainenceData result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
