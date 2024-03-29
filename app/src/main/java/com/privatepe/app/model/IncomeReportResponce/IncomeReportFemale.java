package com.privatepe.app.model.IncomeReportResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeReportFemale {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private IncomeReportData result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public IncomeReportData getResult() {
        return result;
    }

    public void setResult(IncomeReportData result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
