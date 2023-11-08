package com.privatepe.app.model.IncomeReportResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncomeReportData {
    @SerializedName("user_id")
    @Expose
    private Integer userId;


    @SerializedName("points")
    @Expose
    private int points;


    @SerializedName("redeem_point")
    @Expose
    private Integer redeemPoint;


    @SerializedName("amount_inr")
    @Expose
    private String amountInr;

    @SerializedName("amount_dollor")
    @Expose
    private String amountDollor;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Integer getRedeemPoint() {
        return redeemPoint;
    }

    public void setRedeemPoint(Integer redeemPoint) {
        this.redeemPoint = redeemPoint;
    }

    public String getAmountInr() {
        return amountInr;
    }

    public void setAmountInr(String amountInr) {
        this.amountInr = amountInr;
    }

    public String getAmountDollor() {
        return amountDollor;
    }

    public void setAmountDollor(String amountDollor) {
        this.amountDollor = amountDollor;
    }
}
