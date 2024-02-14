package com.privatepe.app.response.daily_weekly;

import java.io.Serializable;
import java.util.List;

public class DailyUserListResponse implements Serializable {

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

        long id, profile_id, charm_level, daily_earning_beans;
        String name,gender;
        List<UserPics> profile_images;
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

        public long getProfile_id() {
            return profile_id;
        }

        public void setProfile_id(long profile_id) {
            this.profile_id = profile_id;
        }

        public long getCharm_level() {
            return charm_level;
        }

        public void setCharm_level(long charm_level) {
            this.charm_level = charm_level;
        }

        public long getDaily_earning_beans() {
            return daily_earning_beans;
        }

        public void setDaily_earning_beans(long daily_earning_beans) {
            this.daily_earning_beans = daily_earning_beans;
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
