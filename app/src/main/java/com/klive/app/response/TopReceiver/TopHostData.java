package com.klive.app.response.TopReceiver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopHostData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("mobile")
    @Expose
    private Long mobile;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("last_week_reward")
    @Expose
    private Integer last_week_reward;
    @SerializedName("profile_images")
    @Expose
    private List<ProfileImage> profileImages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<ProfileImage> getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(List<ProfileImage> profileImages) {
        this.profileImages = profileImages;
    }

    public Integer getLast_week_reward() {
        return last_week_reward;
    }

    public void setLast_week_reward(Integer last_week_reward) {
        this.last_week_reward = last_week_reward;
    }
}