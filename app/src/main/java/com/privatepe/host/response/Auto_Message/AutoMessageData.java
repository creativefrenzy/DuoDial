package com.privatepe.host.response.Auto_Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoMessageData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("messagedetails")
    @Expose
    private List<Messagedetail> messagedetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public List<Messagedetail> getMessagedetails() {
        return messagedetails;
    }

    public void setMessagedetails(List<Messagedetail> messagedetails) {
        this.messagedetails = messagedetails;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
