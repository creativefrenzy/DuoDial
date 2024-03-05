package com.privatepe.host.response.metend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class FollowUserData implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("profile_id")
    public int profile_id;
    @SerializedName("name")
    public String name;
    @SerializedName("level")
    public int level;
    @SerializedName("profile_images")
    public ArrayList<FollowProfileImg> profile_images;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<FollowProfileImg> getProfile_images() {
        return profile_images;
    }

    public void setProfile_images(ArrayList<FollowProfileImg> profile_images) {
        this.profile_images = profile_images;
    }
}
