package com.klive.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingHistoryResult {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("user_id")
    @Expose
    int user_id;

    @SerializedName("reference_id")
    @Expose
    int reference_id;

    @SerializedName("credit")
    @Expose
    int credit;

    @SerializedName("debit")
    @Expose
    int debit;

    @SerializedName("points")
    @Expose
    int points;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("pending")
    @Expose
    int pending;

    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("transaction_des")
    @Expose
    String transaction_des;

    @SerializedName("userdetails")
    @Expose
    TradingHistoryUserDetail tradingHistoryUserDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReference_id() {
        return reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTransaction_des() {
        return transaction_des;
    }

    public void setTransaction_des(String transaction_des) {
        this.transaction_des = transaction_des;
    }

    public TradingHistoryUserDetail getTradingHistoryUserDetail() {
        return tradingHistoryUserDetail;
    }

    public void setTradingHistoryUserDetail(TradingHistoryUserDetail tradingHistoryUserDetail) {
        this.tradingHistoryUserDetail = tradingHistoryUserDetail;
    }
}
