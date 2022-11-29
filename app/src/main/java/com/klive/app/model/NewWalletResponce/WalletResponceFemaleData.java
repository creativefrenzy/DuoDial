package com.klive.app.model.NewWalletResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletResponceFemaleData {

    @SerializedName("coin-with-income-report")
    @Expose
    private CoinWithIncomeReport coinWithIncomeReport;

    @SerializedName("current_week-report")
    @Expose
    private CurrentWeekReport currentWeekReport;

    @SerializedName("last_week-report")
    @Expose
    private LastWeekReport lastWeekReport;

    @SerializedName("today_report")
    @Expose
    private TodayReport todayReport;
    @SerializedName("agent_id")
    @Expose
    private String agentId;

    public CurrentWeekReport getCurrentWeekReport() {
        return currentWeekReport;
    }

    public void setCurrentWeekReport(CurrentWeekReport currentWeekReport) {
        this.currentWeekReport = currentWeekReport;
    }

    public LastWeekReport getLastWeekReport() {
        return lastWeekReport;
    }

    public void setLastWeekReport(LastWeekReport lastWeekReport) {
        this.lastWeekReport = lastWeekReport;
    }

    public TodayReport getTodayReport() {
        return todayReport;
    }

    public void setTodayReport(TodayReport todayReport) {
        this.todayReport = todayReport;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public CoinWithIncomeReport getCoinWithIncomeReport() {
        return coinWithIncomeReport;
    }

    public void setCoinWithIncomeReport(CoinWithIncomeReport coinWithIncomeReport) {
        this.coinWithIncomeReport = coinWithIncomeReport;
    }
}
