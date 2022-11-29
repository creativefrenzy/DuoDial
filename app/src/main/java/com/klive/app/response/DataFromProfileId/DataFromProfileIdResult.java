package com.klive.app.response.DataFromProfileId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFromProfileIdResult {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("profile_id")
    @Expose
    private int profile_id;

    @SerializedName("level")
    @Expose
    private int level;


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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
