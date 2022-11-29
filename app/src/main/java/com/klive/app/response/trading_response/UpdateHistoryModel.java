package com.klive.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateHistoryModel {

    @SerializedName("wallet_id")
    @Expose
    String wallet_Id;

    @SerializedName("type")
    @Expose
    String type;

    public UpdateHistoryModel(String wallet_Id, String type) {
        this.wallet_Id = wallet_Id;
        this.type = type;
    }

    public String getWallet_Id() {
        return wallet_Id;
    }

    public void setWallet_Id(String wallet_Id) {
        this.wallet_Id = wallet_Id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
