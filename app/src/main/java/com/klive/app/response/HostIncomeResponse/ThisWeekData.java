package com.klive.app.response.HostIncomeResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThisWeekData {

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;
    @SerializedName("total_call_coins")
    @Expose
    private String totalCallCoins;
    @SerializedName("total_gift_coins")
    @Expose
    private String totalGiftCoins;
    @SerializedName("total_reward_coins")
    @Expose
    private String totalRewardCoins;
    @SerializedName("total_payout")
    @Expose
    private String totalPayout;
    @SerializedName("payout_dollar")
    @Expose
    private String payoutDollar;
    @SerializedName("settlement_cycle")
    @Expose
    private String settlementCycle;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
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

    public String getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(String totalPayout) {
        this.totalPayout = totalPayout;
    }

    public String getPayoutDollar() {
        return payoutDollar;
    }

    public void setPayoutDollar(String payoutDollar) {
        this.payoutDollar = payoutDollar;
    }

    public String getSettlementCycle() {
        return settlementCycle;
    }

    public void setSettlementCycle(String settlementCycle) {
        this.settlementCycle = settlementCycle;
    }


    public void setTotalRewardCoins(String totalRewardCoins) {
        this.totalRewardCoins = totalRewardCoins;
    }

    public String getTotalRewardCoins() {
        return totalRewardCoins;
    }


}
