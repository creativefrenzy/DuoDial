package com.privatepe.host.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCallPoints {
    @SerializedName("total_point")
    @Expose
    private Integer totalPoint;

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }
}
