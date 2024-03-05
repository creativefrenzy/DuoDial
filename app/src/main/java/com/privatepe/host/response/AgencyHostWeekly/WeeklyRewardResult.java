package com.privatepe.host.response.AgencyHostWeekly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeeklyRewardResult {

    @SerializedName("weeklyrewards")
    @Expose
    ArrayList<WeeklyRewardModel> weeklyRewardModelList;

    @SerializedName("totalreward")
    @Expose
    private int totalreward;

    public ArrayList<WeeklyRewardModel> getWeeklyRewardModelList() {
        return weeklyRewardModelList;
    }

    public void setWeeklyRewardModelList(ArrayList<WeeklyRewardModel> weeklyRewardModellist) {
        this.weeklyRewardModelList = weeklyRewardModellist;
    }

    public int getTotalreward() {
        return totalreward;
    }

    public void setTotalreward(int totalreward) {
        this.totalreward = totalreward;
    }
}
