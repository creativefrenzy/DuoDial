package com.klive.app.model.fcm;

public class Data {
    private String title;
    private String account;
    private String msg;
    private String profileName;
    private String profileImage;
    private String type;

    public Data(String title, String account, String msg, String profileName, String profileImage, String type) {
        this.title = title;
        this.account = account;
        this.msg = msg;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.type = type;
    }

    public Data() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
