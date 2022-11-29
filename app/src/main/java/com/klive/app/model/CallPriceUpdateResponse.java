package com.klive.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.klive.app.model.PriceList.PriceDataModel;

import java.util.List;

public class CallPriceUpdateResponse {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private List<PriceDataModel> result = null;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PriceDataModel> getResult() {
        return result;
    }

    public void setResult(List<PriceDataModel> result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
