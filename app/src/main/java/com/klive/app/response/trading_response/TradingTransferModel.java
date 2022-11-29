package com.klive.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingTransferModel {

    @SerializedName("profile_id")
    @Expose
    String userId;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("type")
    @Expose
    String type;

    public TradingTransferModel(String userId, String amount, String type) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
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


}
