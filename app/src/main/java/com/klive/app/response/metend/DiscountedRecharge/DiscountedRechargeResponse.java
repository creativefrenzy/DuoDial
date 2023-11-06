package com.klive.app.response.metend.DiscountedRecharge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountedRechargeResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("is_recharge")
    @Expose
    private int isRecharge;

    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getIsRecharge() {
        return isRecharge;
    }

    public void setIsRecharge(int isRecharge) {
        this.isRecharge = isRecharge;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
