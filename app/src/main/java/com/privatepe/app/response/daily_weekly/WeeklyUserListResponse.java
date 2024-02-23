package com.privatepe.app.response.daily_weekly;

import java.io.Serializable;
import java.util.List;

public class WeeklyUserListResponse implements Serializable {

    boolean success;
    List<Result> result;
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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result implements Serializable {

        long id;
        int total_coin_earned,reward_coin,rank,status;
        List<UserPics> profile_images;
        WeeklyUserData user;

        public WeeklyUserData getUser() {
            return user;
        }

        public void setUser(WeeklyUserData user) {
            this.user = user;
        }

        public List<UserPics> getProfile_images() {
            return profile_images;
        }

        public void setProfile_images(List<UserPics> profile_images) {
            this.profile_images = profile_images;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getTotal_coin_earned() {
            return total_coin_earned;
        }

        public void setTotal_coin_earned(int total_coin_earned) {
            this.total_coin_earned = total_coin_earned;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class WeeklyUserData implements Serializable {
        long charm_level,profile_id;
        String name,gender;
        public long getCharm_level() {
            return charm_level;
        }
        public void setCharm_level(long charm_level) {
            this.charm_level = charm_level;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getGender() {
            return gender;
        }
        public void setGender(String gender) {
            this.gender = gender;
        }
        public long getProfile_id() {
            return profile_id;
        }
        public void setProfile_id(long profile_id) {
            this.profile_id = profile_id;
        }
    }
    public static class UserPics implements Serializable {
        long id, user_id;
        String image_name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }
    }

}
