package com.klive.app.response.metend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class FollowingUserData implements Serializable {

    @SerializedName("current_page")
    public int current_page;
    @SerializedName("data")
    public ArrayList<FollowingDatum> data;
    @SerializedName("first_page_url")
    public String first_page_url;
    @SerializedName("from")
    public int from;
    @SerializedName("last_page")
    public int last_page;
    @SerializedName("last_page_url")
    public String last_page_url;
    @SerializedName("next_page_url")
    public Object next_page_url;
    @SerializedName("path")
    public String path;
    @SerializedName("per_page")
    public int per_page;
    @SerializedName("prev_page_url")
    public Object prev_page_url;
    @SerializedName("to")
    public int to;
    @SerializedName("total")
    public int total;

    public ArrayList<FollowingDatum> getData() {
        return data;
    }

    public void setData(ArrayList<FollowingDatum> data) {
        this.data = data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
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

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
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

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
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
