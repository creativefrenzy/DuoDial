package com.privatepe.app.response.AgencyDate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgencyDateResult {

    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;
    @SerializedName("total_payout")
    @Expose
    private Float totalPayout;
    @SerializedName("week_name")
    @Expose
    private Integer weekName;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("settlement_cycle")
    @Expose
    private String settlementCycle;

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }

    public Float getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(Float totalPayout) {
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

}