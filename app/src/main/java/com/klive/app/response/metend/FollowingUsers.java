package com.klive.app.response.metend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FollowingUsers implements Serializable {

    @SerializedName("success")
    public boolean success;
    @SerializedName("result")
    public FollowingUserData followingUserData;
    public Object error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public FollowingUserData getFollowingUserData() {
        return followingUserData;
    }

    public void setFollowingUserData(FollowingUserData followingUserData) {
        this.followingUserData = followingUserData;
    }
}
