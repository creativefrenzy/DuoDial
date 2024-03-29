package com.privatepe.app.response.metend.PaymentGatewayDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("stripe")
    @Expose
    private Stripe stripe;
    @SerializedName("razorpay")
    @Expose
    private Razorpay razorpay;
    @SerializedName("balance")
    @Expose
    private Balance balance;

    public Stripe getStripe() {
        return stripe;
    }

    public void setStripe(Stripe stripe) {
        this.stripe = stripe;
    }

    public Razorpay getRazorpay() {
        return razorpay;
    }

    public void setRazorpay(Razorpay razorpay) {
        this.razorpay = razorpay;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
