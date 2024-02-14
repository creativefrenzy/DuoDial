package com.privatepe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileDetailsResponse {

    ProfileData success;

    public ProfileData getSuccess() {
        return success;
    }

  /*    public static class ResultDataNewProfile {

        int id, favorite_count, favorite_by_you_count, profile_id, call_rate, is_online;
        String name, username, dob, about_user;
        List<UserListResponse.UserPics> profile_images;
        long mobile;
    }*/

    public static class ProfileData implements Serializable  {
        int id, favorite_count, favorite_by_you_count, profile_id, call_rate, is_online, is_busy, audio_call_rate, level,rich_level,charm_level, total_coins;
        String name, username, dob, about_user, city, login_type, firebase_status,android_id;
        List<UserListResponse.UserPics> profile_images;
        List<UserListResponse.Language> user_languages;

        @SerializedName("profile_video")
        @Expose
        private List<UserListResponse.ProfileVideo> profileVideo = null;

        public int getRich_level() {
            return rich_level;
        }

        public void setRich_level(int rich_level) {
            this.rich_level = rich_level;
        }

        public int getCharm_level() {
            return charm_level;
        }

        public void setCharm_level(int charm_level) {
            this.charm_level = charm_level;
        }

        public int getTotal_coins() {
            return total_coins;
        }

        public int getLevel() {
            return level;
        }

        long mobile;
        public int getAudio_call_rate() {
            return audio_call_rate;
        }

        public void setAudio_call_rate(int audio_call_rate) {
            this.audio_call_rate = audio_call_rate;
        }

        public List<UserListResponse.Language> getUser_languages() {
            return user_languages;
        }

        public String getFirebase_status() {
            return firebase_status;
        }

        public void setFirebase_status(String firebase_status) {
            this.firebase_status = firebase_status;
        }

        public String getAndroid_id() {
            return android_id;
        }

        public String getLogin_type() {
            return login_type;
        }

        public int getIs_busy() {
            return is_busy;
        }

        public String getCity() {
            return city;
        }

        public long getMobile() {
            return mobile;
        }

        public int getCall_rate() {
            return call_rate;
        }

        public int getIs_online() {
            return is_online;
        }

        public String getDob() {
            return dob;
        }

        public String getAbout_user() {
            return about_user;
        }

        public int getId() {
            return id;
        }

        public int getFavorite_count() {
            return favorite_count;
        }

        public int getFavorite_by_you_count() {
            return favorite_by_you_count;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public int getProfile_id() {
            return profile_id;
        }

        public List<UserListResponse.UserPics> getProfile_images() {
            return profile_images;
        }

        /////////////

        public void setId(int id) {
            this.id = id;
        }

        public void setFavorite_count(int favorite_count) {
            this.favorite_count = favorite_count;
        }

        public void setFavorite_by_you_count(int favorite_by_you_count) {
            this.favorite_by_you_count = favorite_by_you_count;
        }

        public void setProfile_id(int profile_id) {
            this.profile_id = profile_id;
        }

        public void setCall_rate(int call_rate) {
            this.call_rate = call_rate;
        }

        public void setIs_online(int is_online) {
            this.is_online = is_online;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public void setAbout_user(String about_user) {
            this.about_user = about_user;
        }

        public void setProfile_images(List<UserListResponse.UserPics> profile_images) {
            this.profile_images = profile_images;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }


        public List<UserListResponse.ProfileVideo> getProfileVideo() {
            return profileVideo;
        }

        public void setProfileVideo(List<UserListResponse.ProfileVideo> profileVideo) {
            this.profileVideo = profileVideo;
        }
    }

    public static class UserPics implements Serializable {
        int id, user_id, is_profile_image;
        String image_name, image_type, created_at, updated_at;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getIs_profile_image() {
            return is_profile_image;
        }

        public String getImage_name() {
            return image_name;
        }

        public String getImage_type() {
            return image_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        /////////////

        public void setId(int id) {
            this.id = id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setIs_profile_image(int is_profile_image) {
            this.is_profile_image = is_profile_image;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public void setImage_type(String image_type) {
            this.image_type = image_type;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
    public static class Language implements Serializable {
        int id, user_id, language_id;
        String language_name;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getLanguage_id() {
            return language_id;
        }

        public String getLanguage_name() {
            return language_name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setLanguage_id(int language_id) {
            this.language_id = language_id;
        }

        public void setLanguage_name(String language_name) {
            this.language_name = language_name;
        }
    }


}
