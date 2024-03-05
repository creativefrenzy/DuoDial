package com.privatepe.host.response.metend.new_notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotiProfileImages {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("image_name")
    @Expose
    private String imageName;

    @SerializedName("is_profile_image")
    @Expose
    private int isProfileImage;

    @SerializedName("image_type")
    @Expose
    private String imageType;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getIsProfileImage() {
        return isProfileImage;
    }

    public void setIsProfileImage(int isProfileImage) {
        this.isProfileImage = isProfileImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
