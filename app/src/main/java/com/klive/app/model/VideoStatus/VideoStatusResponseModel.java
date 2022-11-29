package com.klive.app.model.VideoStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.klive.app.model.PriceList.PriceDataModel;

import java.util.List;

public class VideoStatusResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private List<StatusDataModel> result = null;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StatusDataModel> getResult() {
        return result;
    }

    public void setResult(List<StatusDataModel> result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}



