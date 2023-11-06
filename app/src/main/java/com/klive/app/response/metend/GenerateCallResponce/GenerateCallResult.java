package com.klive.app.response.metend.GenerateCallResponce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCallResult {
    @SerializedName("data")
    @Expose
    private GenerateCallData data;
    @SerializedName("points")
    @Expose
    private GenerateCallPoints points;

    public GenerateCallData getData() {
        return data;
    }

    public void setData(GenerateCallData data) {
        this.data = data;
    }

    public GenerateCallPoints getPoints() {
        return points;
    }

    public void setPoints(GenerateCallPoints points) {
        this.points = points;
    }
}
