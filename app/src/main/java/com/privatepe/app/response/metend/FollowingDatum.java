package com.privatepe.app.response.metend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FollowingDatum implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("follower_id")
    public int follower_id;
    @SerializedName("following_id")
    public int following_id;
    @SerializedName("following_data")
    public FollowUserData following_data;

    public FollowUserData getFollowing_data() {
        return following_data;
    }

    public void setFollowing_data(FollowUserData following_data) {
        this.following_data = following_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(int follower_id) {
        this.follower_id = follower_id;
    }

    public int getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(int following_id) {
        this.following_id = following_id;
    }

}
