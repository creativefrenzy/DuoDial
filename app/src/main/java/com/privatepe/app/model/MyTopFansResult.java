package com.privatepe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTopFansResult {
    @SerializedName("current_page")
    @Expose
    public int current_page;
    @SerializedName("data")
    @Expose
    public List<MyTopFansData> data;
    @SerializedName("first_page_url")
    @Expose
    public String first_page_url;
    @SerializedName("from")
    @Expose
    public int from;
    @SerializedName("last_page")
    @Expose
    public int last_page;
    @SerializedName("last_page_url")
    @Expose
    public String last_page_url;
    @SerializedName("next_page_url")
    @Expose
    public Object next_page_url;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("per_page")
    @Expose
    public int per_page;
    @SerializedName("prev_page_url")
    @Expose
    public Object prev_page_url;
    @SerializedName("to")
    @Expose
    public int to;
    @SerializedName("total")
    @Expose
    public int total;
}
