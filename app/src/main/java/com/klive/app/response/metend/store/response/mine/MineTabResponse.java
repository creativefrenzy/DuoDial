package com.klive.app.response.metend.store.response.mine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.klive.app.response.metend.store_list.StoreResultModel;

import java.util.List;

public class MineTabResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<StoreResultModel> result;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StoreResultModel> getResult() {
        return result;
    }

    public void setResult(List<StoreResultModel> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
