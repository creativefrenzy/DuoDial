package com.privatepe.host.model.LevelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentWeekReport {
    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;
    @SerializedName("level")
    @Expose
    private Integer level;
    public Integer getTotalCoins() {
        return totalCoins;
    }
    public Integer getLevel() {
        return level;
    }
    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

}
