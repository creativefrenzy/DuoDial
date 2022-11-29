package com.klive.app.response.TopReceiver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopReceiverResult {
    @SerializedName("total_coins")
    @Expose
    private String totalCoins;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("razorpay_id")
    @Expose
    private String razorpayId;
    @SerializedName("transaction_des")
    @Expose
    private String transactionDes;
    @SerializedName("female")
    @Expose
    private TopHostData female;

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRazorpayId() {
        return razorpayId;
    }

    public void setRazorpayId(String razorpayId) {
        this.razorpayId = razorpayId;
    }

    public String getTransactionDes() {
        return transactionDes;
    }

    public void setTransactionDes(String transactionDes) {
        this.transactionDes = transactionDes;
    }

    public TopHostData getFemale() {
        return female;
    }

    public void setFemale(TopHostData female) {
        this.female = female;
    }

}
