package com.privatepe.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingAccount {
    @SerializedName("is_trade_account")
    @Expose
    private int isTradeAccount;

    @SerializedName("total_points")
    @Expose
    private int totalPonts;


    public int getIsTradeAccount() {
        return isTradeAccount;
    }

    public void setIsTradeAccount(int isTradeAccount) {
        this.isTradeAccount = isTradeAccount;
    }

    public int getTotalPonts() {
        return totalPonts;
    }

    public void setTotalPonts(int totalPonts) {
        this.totalPonts = totalPonts;
    }
}
