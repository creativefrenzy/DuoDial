package com.privatepe.host.response.trading_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserIdBodyModel {
    @SerializedName("profile_id")
    @Expose
    String userId;

    @SerializedName("type")
    @Expose
    String type;

    public UserIdBodyModel(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
