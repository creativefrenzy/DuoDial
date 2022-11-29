package com.klive.app.model.Walletfilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {



    @SerializedName("walletHistory")
    @Expose
    private List<FilterwalletHistory> walletHistory = null;

    public List<FilterwalletHistory> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(List<FilterwalletHistory> walletHistory) {
        this.walletHistory = walletHistory;
    }

}
