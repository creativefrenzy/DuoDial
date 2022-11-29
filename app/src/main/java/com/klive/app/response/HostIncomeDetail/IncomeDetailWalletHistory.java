package com.klive.app.response.HostIncomeDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeDetailWalletHistory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("credit")
    @Expose
    private Integer credit;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("razorpay_id")
    @Expose
    private String razorpayId;
    @SerializedName("transaction_des")
    @Expose
    private String transactionDes;
    @SerializedName("caller_profile_id")
    @Expose
    private Integer callerProfileId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getCallerProfileId() {
        return callerProfileId;
    }

    public void setCallerProfileId(Integer callerProfileId) {
        this.callerProfileId = callerProfileId;
    }

}
