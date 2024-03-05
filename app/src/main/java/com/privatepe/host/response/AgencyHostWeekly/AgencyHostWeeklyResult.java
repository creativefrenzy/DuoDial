package com.privatepe.host.response.AgencyHostWeekly;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AgencyHostWeeklyResult {

    @SerializedName("giftByHostsRecord")
    @Expose
    private List<GiftByHostsRecord> giftByHostsRecord = null;
    @SerializedName("totalPayout")
    @Expose
    private Float totalPayout;
    @SerializedName("totalPayoutDollar")
    @Expose
    private Float totalPayoutDollar;
    @SerializedName("commission_ratio")
    @Expose
    private Integer commissionRatio;
    @SerializedName("agency_amount")
    @Expose
    private Double agencyAmount;
    @SerializedName("totalCoins")
    @Expose
    private Integer totalCoins;
    @SerializedName("query_date_start")
    @Expose
    private String queryDateStart;
    @SerializedName("tds")
    @Expose
    private Double tds;
    @SerializedName("finalPayout")
    @Expose
    private Double finalPayout;
    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("subagency_amount")
    @Expose
    private Double subagencyAmount;


    @SerializedName("subagency_totalPayout")
    @Expose
    private Integer subagencyTotalPayout;
    @SerializedName("subagency_totalPayoutDollar")
    @Expose
    private Double subagencyTotalPayoutDollar;
    @SerializedName("subagency_commission_ratio")
    @Expose
    private Integer subagencyCommissionRatio;

    public List<GiftByHostsRecord> getGiftByHostsRecord() {
        return giftByHostsRecord;
    }

    public void setGiftByHostsRecord(List<GiftByHostsRecord> giftByHostsRecord) {
        this.giftByHostsRecord = giftByHostsRecord;
    }

    public Float getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(Float totalPayout) {
        this.totalPayout = totalPayout;
    }

    public Float getTotalPayoutDollar() {
        return totalPayoutDollar;
    }

    public void setTotalPayoutDollar(Float totalPayoutDollar) {
        this.totalPayoutDollar = totalPayoutDollar;
    }

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public Double getAgencyAmount() {
        return agencyAmount;
    }

    public void setAgencyAmount(Double agencyAmount) {
        this.agencyAmount = agencyAmount;
    }

    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
    }

    public String getQueryDateStart() {
        return queryDateStart;
    }

    public void setQueryDateStart(String queryDateStart) {
        this.queryDateStart = queryDateStart;
    }

    public Double getTds() {
        return tds;
    }

    public void setTds(Double tds) {
        this.tds = tds;
    }

    public Double getFinalPayout() {
        return finalPayout;
    }

    public void setFinalPayout(Double finalPayout) {
        this.finalPayout = finalPayout;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getSubagencyAmount() {
        return subagencyAmount;
    }

    public void setSubagencyAmount(Double subagencyAmount) {
        this.subagencyAmount = subagencyAmount;
    }

    public Integer getSubagencyTotalPayout() {
        return subagencyTotalPayout;
    }

    public void setSubagencyTotalPayout(Integer subagencyTotalPayout) {
        this.subagencyTotalPayout = subagencyTotalPayout;
    }

    public Double getSubagencyTotalPayoutDollar() {
        return subagencyTotalPayoutDollar;
    }

    public void setSubagencyTotalPayoutDollar(Double subagencyTotalPayoutDollar) {
        this.subagencyTotalPayoutDollar = subagencyTotalPayoutDollar;
    }

    public Integer getSubagencyCommissionRatio() {
        return subagencyCommissionRatio;
    }

    public void setSubagencyCommissionRatio(Integer subagencyCommissionRatio) {
        this.subagencyCommissionRatio = subagencyCommissionRatio;
    }
}
