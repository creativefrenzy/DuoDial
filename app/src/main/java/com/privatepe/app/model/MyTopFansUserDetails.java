package com.privatepe.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.privatepe.app.response.TopReceiver.ProfileImage;

import java.util.ArrayList;

public class MyTopFansUserDetails {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("is_online")
    @Expose
    public int is_online;
    @SerializedName("rich_level")
    @Expose
    public int rich_level;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("favorite_by_you_count")
    @Expose
    public int favorite_by_you_count;
    @SerializedName("profile_images")
    @Expose
    public ArrayList<ProfileImage> profile_images;
}
