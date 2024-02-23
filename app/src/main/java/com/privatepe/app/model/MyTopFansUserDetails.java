package com.privatepe.app.model;

import com.privatepe.app.response.TopReceiver.ProfileImage;

import java.util.ArrayList;

public class MyTopFansUserDetails {
    public int id;
    public String name;
    public int is_online;
    public int rich_level;
    public int gender;
    public int favorite_by_you_count;
    public ArrayList<ProfileImage> profile_images;
}
