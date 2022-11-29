package com.klive.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WalletHistoryPaginationModel {

    @SerializedName("current_page")
    @Expose
    int current_page;

    @SerializedName("data")
    @Expose
    ArrayList<TradingHistoryResult> data;



    @SerializedName("first_page_url")
    @Expose
    String first_page_url;

    @SerializedName("from")
    @Expose
    int from;

    @SerializedName("last_page")
    @Expose
    int last_page;

    @SerializedName("last_page_url")
    @Expose
    String last_page_url;

    @SerializedName("next_page_url")
    @Expose
    String next_page_url;
    @SerializedName("path")
    @Expose
    String path;

    @SerializedName("per_page")
    @Expose
    int per_page;

    @SerializedName("prev_page_url")
    @Expose
    String prev_page_url;

    @SerializedName("to")
    @Expose
    int to;

    @SerializedName("total")
    @Expose
    int total;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<TradingHistoryResult> getData() {
        return data;
    }

    public void setData(ArrayList<TradingHistoryResult> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
