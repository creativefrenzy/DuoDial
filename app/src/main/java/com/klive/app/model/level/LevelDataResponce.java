package com.klive.app.model.level;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelDataResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private LevelDataResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public LevelDataResult getResult() {
        return result;
    }

    public void setResult(LevelDataResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
