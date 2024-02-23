package com.privatepe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyTopFansData {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("sender_id")
    @Expose
    public int sender_id;
    @SerializedName("total_beans")
    @Expose
    public int total_beans;
    @SerializedName("updated_at")
    @Expose
    public Date updated_at;
    @SerializedName("user_details")
    @Expose
    public MyTopFansUserDetails user_details;
}
