package com.klive.app.model.PaymentRequestResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentRequestResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("amount_inr")
    @Expose
    private Double amountInr;
    @SerializedName("payout_id")
    @Expose
    private String payoutId;
    @SerializedName("razorpay_status")
    @Expose
    private String razorpayStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("payment_request_account")
    @Expose
    private PaymentRequestAccount paymentRequestAccount;



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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getAmountInr() {
        return amountInr;
    }

    public void setAmountInr(Double amountInr) {
        this.amountInr = amountInr;
    }

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }

    public String getRazorpayStatus() {
        return razorpayStatus;
    }

    public void setRazorpayStatus(String razorpayStatus) {
        this.razorpayStatus = razorpayStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PaymentRequestAccount getPaymentRequestAccount() {
        return paymentRequestAccount;
    }

    public void setPaymentRequestAccount(PaymentRequestAccount paymentRequestAccount) {
        this.paymentRequestAccount = paymentRequestAccount;
    }

}


