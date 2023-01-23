package com.klive.app.model;

public class FirebasePopupMsgModel {

    private String userId;
    private String userName;
    private String Message;
    private String profilePic;

    public FirebasePopupMsgModel(String userId, String userName, String message, String profilePic) {
        this.userId = userId;
        this.userName = userName;
        Message = message;
        this.profilePic = profilePic;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
