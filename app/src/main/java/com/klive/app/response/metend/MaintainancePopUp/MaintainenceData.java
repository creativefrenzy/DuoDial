package com.klive.app.response.metend.MaintainancePopUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaintainenceData {
    @SerializedName("down")
    @Expose
    private Integer down;

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }
}
