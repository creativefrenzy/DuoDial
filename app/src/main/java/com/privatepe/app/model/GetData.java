package com.privatepe.app.model;

import com.google.gson.annotations.SerializedName;

public class GetData {
    @SerializedName("token")
    private String token;
    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profile_id")
    private String profile_id;
    @SerializedName("username")
    private String username;
    @SerializedName("login_type")
    private String login_type;
    @SerializedName("is_online")
    private int is_online;
    @SerializedName("allow_in_app_purchase")
    private int allow_in_app_purchase;
    @SerializedName("country")
    private String country;
    @SerializedName("user_city")
    private String user_city;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public String getUsername() {
        return username;
    }

    public String getLogin_type() {
        return login_type;
    }

    public int getIs_online() {
        return is_online;
    }

    public int getAllow_in_app_purchase() {
        return allow_in_app_purchase;
    }

    public String getCountry() {
        return country;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public void setAllow_in_app_purchase(int allow_in_app_purchase) {
        this.allow_in_app_purchase = allow_in_app_purchase;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }
}
