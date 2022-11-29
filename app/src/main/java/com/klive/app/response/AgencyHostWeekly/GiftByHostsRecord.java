package com.klive.app.response.AgencyHostWeekly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftByHostsRecord {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("is_admin_block")
    @Expose
    private Integer isAdminBlock;
    @SerializedName("total_coins_earned")
    @Expose
    private String totalCoinsEarned;
    @SerializedName("payout")
    @Expose
    private String payout;
    @SerializedName("payout_dollar")
    @Expose
    private String payoutDollar;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_coins_blocked")
    @Expose
    private String totalCoinsBlocked;
    @SerializedName("total_coins")
    @Expose
    private String totalCoins;
    @SerializedName("total_call_coins")
    @Expose
    private String totalCallCoins;
    @SerializedName("total_gift_coins")
    @Expose
    private String totalGiftCoins;
    @SerializedName("payout_status")
    @Expose
    private String payoutStatus;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsAdminBlock() {
        return isAdminBlock;
    }

    public void setIsAdminBlock(Integer isAdminBlock) {
        this.isAdminBlock = isAdminBlock;
    }

    public String getTotalCoinsEarned() {
        return totalCoinsEarned;
    }

    public void setTotalCoinsEarned(String totalCoinsEarned) {
        this.totalCoinsEarned = totalCoinsEarned;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public String getPayoutDollar() {
        return payoutDollar;
    }

    public void setPayoutDollar(String payoutDollar) {
        this.payoutDollar = payoutDollar;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTotalCoinsBlocked() {
        return totalCoinsBlocked;
    }

    public void setTotalCoinsBlocked(String totalCoinsBlocked) {
        this.totalCoinsBlocked = totalCoinsBlocked;
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

    public String getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(String payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

}