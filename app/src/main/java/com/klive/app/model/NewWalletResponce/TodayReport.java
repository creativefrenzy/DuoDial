package com.klive.app.model.NewWalletResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayReport {

    @SerializedName("total_gifts")
    @Expose
    private Integer totalGifts;
    @SerializedName("total_video_coins")
    @Expose
    private String totalVideoCoins;
    @SerializedName("total_audio_coins")
    @Expose
    private Integer totalAudioCoins;
    @SerializedName("total_coins")
    @Expose
    private Integer totalCoins;

    public Integer getTotalGifts() {
        return totalGifts;
    }

    public void setTotalGifts(Integer totalGifts) {
        this.totalGifts = totalGifts;
    }

    public String getTotalVideoCoins() {
        return totalVideoCoins;
    }

    public void setTotalVideoCoins(String totalVideoCoins) {
        this.totalVideoCoins = totalVideoCoins;
    }

    public Integer getTotalAudioCoins() {
        return totalAudioCoins;
    }

    public void setTotalAudioCoins(Integer totalAudioCoins) {
        this.totalAudioCoins = totalAudioCoins;
    }

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }

}
