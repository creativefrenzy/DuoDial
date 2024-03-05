package com.privatepe.host.response.HostIncomeResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class IncomeResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private IncomeResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public IncomeResult getResult() {
        return result;
    }

    public void setResult(IncomeResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}

