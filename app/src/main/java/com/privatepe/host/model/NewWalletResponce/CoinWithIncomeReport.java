package com.privatepe.host.model.NewWalletResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinWithIncomeReport {
    @SerializedName("total_coins")
    @Expose
    private String totalCoins;
    @SerializedName("total_money_dollor")
    @Expose
    private String totalMoneyDollor;
    @SerializedName("total_money_inr")
    @Expose
    private String totalMoneyInr;

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

    public String getTotalMoneyDollor() {
        return totalMoneyDollor;
    }

    public void setTotalMoneyDollor(String totalMoneyDollor) {
        this.totalMoneyDollor = totalMoneyDollor;
    }

    public String getTotalMoneyInr() {
        return totalMoneyInr;
    }

    public void setTotalMoneyInr(String totalMoneyInr) {
        this.totalMoneyInr = totalMoneyInr;
    }

}
