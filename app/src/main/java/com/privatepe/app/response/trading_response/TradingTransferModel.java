package com.privatepe.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingTransferModel {

    @SerializedName("profile_id")
    @Expose
    String userId;

    @SerializedName("amount")
    @Expose
    String amount;
    @SerializedName("actual_amount")
    @Expose
    String actualAmount;
    @SerializedName("account")
    @Expose
    String account;
    @SerializedName("profileName")
    @Expose
    String profileName;
    @SerializedName("profileImage")
    @Expose
    String profileImage;

    @SerializedName("type")
    @Expose
    String type;

   /* public TradingTransferModel(String userId, String amount, String type) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
    }*/

    public TradingTransferModel(String userId, String amount, String account, String profileName, String profileImage,
                                String type,String actualAmount) {
        this.userId = userId;
        this.amount = amount;
        this.account = account;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.type = type;
        this.actualAmount=actualAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

}
