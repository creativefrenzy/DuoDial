package com.privatepe.host.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatepe.host.model.PriceList.PriceDataModel;

import java.util.List;

public class PriceListResponse {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private List<PriceDataModel> result = null;
    @SerializedName("error")
    @Expose
    private Object error;

    @SerializedName("call_rate")
    @Expose
    private int call_rate;

    @SerializedName("level")
    @Expose
    private int level;


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

    public int getCall_rate() {
        return call_rate;
    }

    public void setCall_rate(int call_rate) {
        this.call_rate = call_rate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
