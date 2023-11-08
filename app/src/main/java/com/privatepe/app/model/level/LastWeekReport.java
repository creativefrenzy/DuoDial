package com.privatepe.app.model.level;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastWeekReport {
    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }
}
