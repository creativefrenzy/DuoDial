package com.privatepe.app.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingHistoryUserDetail {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("profile_id")
    @Expose
    int profile_id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("username")
    @Expose
    String username;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
