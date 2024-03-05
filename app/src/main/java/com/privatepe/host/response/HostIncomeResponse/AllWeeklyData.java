package com.privatepe.host.response.HostIncomeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllWeeklyData {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("total_coins")
    @Expose
    private String totalCoins;
    @SerializedName("total_call_coins")
    @Expose
    private String totalCallCoins;
    @SerializedName("total_gift_coins")
    @Expose
    private String totalGiftCoins;

    @SerializedName("total_reward_coins")
    @Expose
    private String totalRewardCoins;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

    public String getTotalCallCoins() {
        return totalCallCoins;
    }

    public void setTotalCallCoins(String totalCallCoins) {
        this.totalCallCoins = totalCallCoins;
    }

    public String getTotalGiftCoins() {
        return totalGiftCoins;
    }

    public void setTotalGiftCoins(String totalGiftCoins) {
        this.totalGiftCoins = totalGiftCoins;
    }


    public String getTotalRewardCoins() {
        return totalRewardCoins;
    }

    public void setTotalRewardCoins(String totalRewardCoins) {
        this.totalCallCoins = totalRewardCoins;

    }


}
