package com.privatepe.host.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZegoTokenResult {

    @SerializedName("token")
    @Expose
    String token;

    @SerializedName("room_id")
    @Expose
    String roomId;

    public ZegoTokenResult(String token, String roomId) {
        this.token = token;
        this.roomId = roomId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


}
