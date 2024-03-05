package com.privatepe.host.response.AgencyHostWeekly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeeklyRewardResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("result")
    @Expose
    private WeeklyRewardResult weeklyRewardResult;

    @SerializedName("error")
    @Expose
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public WeeklyRewardResult getWeeklyRewardResult() {
        return weeklyRewardResult;
    }

    public void setWeeklyRewardResult(WeeklyRewardResult weeklyRewardResult) {
        this.weeklyRewardResult = weeklyRewardResult;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



}
