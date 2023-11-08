package com.privatepe.app.response.metend.PaytmDirect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmResult {
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("plan_id")
    @Expose
    private Integer planId;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
