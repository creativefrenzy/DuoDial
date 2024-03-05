package com.privatepe.host.response.AgencyHostWeekly;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeeklyRewardModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("reward_coin")
    @Expose
    private int reward_coin;

    @SerializedName("rank")
    @Expose
    private int rank;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReward_coin() {
        return reward_coin;
    }

    public void setReward_coin(int reward_coin) {
        this.reward_coin = reward_coin;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


}
