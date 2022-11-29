package com.klive.app.ZegoExpress.zim;

public class UserInfo {

    private String mUserName;
    private String mUserId;
    private String mIcon;

    public UserInfo() {
    }

    public UserInfo(String id,String name, String icon) {
        this.mUserId = id;
        this.mUserName = name;
        this.mIcon = icon;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }
}
