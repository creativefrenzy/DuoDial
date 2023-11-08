package com.privatepe.app.response.sub_agency;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SubAgencyResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private SubAgencyResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SubAgencyResult getResult() {
        return result;
    }

    public void setResult(SubAgencyResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}

