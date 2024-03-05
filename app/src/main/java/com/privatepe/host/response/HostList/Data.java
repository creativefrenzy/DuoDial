package com.privatepe.host.response.HostList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private Long mobile;
    @SerializedName("is_admin_block")
    @Expose
    private Integer isAdminBlock;
    @SerializedName("demo_password")
    @Expose
    private String demoPassword;
    @SerializedName("level")
    @Expose
    private Integer level;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Integer getIsAdminBlock() {
        return isAdminBlock;
    }

    public void setIsAdminBlock(Integer isAdminBlock) {
        this.isAdminBlock = isAdminBlock;
    }

    public String getDemoPassword() {
        return demoPassword;
    }

    public void setDemoPassword(String demoPassword) {
        this.demoPassword = demoPassword;
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

}

