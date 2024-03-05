package com.privatepe.host.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTradingUserNameResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private GetTradingUserName result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public GetTradingUserName getResult() {
        return result;
    }

    public void setResult(GetTradingUserName result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
