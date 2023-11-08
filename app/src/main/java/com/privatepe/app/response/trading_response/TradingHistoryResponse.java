package com.privatepe.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingHistoryResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("total_points")
    @Expose
    int total_points;

    @SerializedName("result")
    @Expose
    private WalletHistoryPaginationModel result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public WalletHistoryPaginationModel getResult() {
        return result;
    }

    public void setResult(WalletHistoryPaginationModel result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
