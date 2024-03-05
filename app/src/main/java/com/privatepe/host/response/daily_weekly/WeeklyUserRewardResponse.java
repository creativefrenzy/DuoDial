package com.privatepe.host.response.daily_weekly;

import java.io.Serializable;
import java.util.List;

public class WeeklyUserRewardResponse implements Serializable {

    boolean success;
    Result result;
    Object error;

    public boolean isSuccess() {
        return success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result implements Serializable {

        List<WeeklyRewardData> weeklyreward;

        public List<WeeklyRewardData> getWeeklyreward() {
            return weeklyreward;
        }

        public void setWeeklyreward(List<WeeklyRewardData> weeklyreward) {
            this.weeklyreward = weeklyreward;
        }
    }
    public static class WeeklyRewardData implements Serializable {
       int rank;
       int reward_coin;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getReward_coin() {
            return reward_coin;
        }

        public void setReward_coin(int reward_coin) {
            this.reward_coin = reward_coin;
        }
    }


}
