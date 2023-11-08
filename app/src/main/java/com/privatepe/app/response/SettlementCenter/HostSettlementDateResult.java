package com.privatepe.app.response.SettlementCenter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HostSettlementDateResult {

    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;
    @SerializedName("total_payout")
    @Expose
    private float totalPayout;
    @SerializedName("week_name")
    @Expose
    private Integer weekName;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("settlement_cycle")
    @Expose
    private String settlementCycle;
    @SerializedName("commission_ratio")
    @Expose
    private Integer commissionRatio;
    @SerializedName("commsion_amount")
    @Expose
    private float commsionAmount;
    @SerializedName("amount_indollor")
    @Expose
    private Integer amountIndollor;

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }

    public float getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(float totalPayout) {
        this.totalPayout = totalPayout;
    }

    public Integer getWeekName() {
        return weekName;
    }

    public void setWeekName(Integer weekName) {
        this.weekName = weekName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSettlementCycle() {
        return settlementCycle;
    }

    public void setSettlementCycle(String settlementCycle) {
        this.settlementCycle = settlementCycle;
    }

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public float getCommsionAmount() {
        return commsionAmount;
    }

    public void setCommsionAmount(float commsionAmount) {
        this.commsionAmount = commsionAmount;
    }

    public Integer getAmountIndollor() {
        return amountIndollor;
    }

    public void setAmountIndollor(Integer amountIndollor) {
        this.amountIndollor = amountIndollor;
    }

}
