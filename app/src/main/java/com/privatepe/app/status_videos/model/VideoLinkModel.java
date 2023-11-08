package com.privatepe.app.status_videos.model;

public class VideoLinkModel {

    String userId;
    String VideoLink;
    String VideoURI;
    String VideoAddedTimeStamp;


    public VideoLinkModel(String userId, String videoLink, String videoURI, String videoAddedTimeStamp) {
        this.userId = userId;
        VideoLink = videoLink;
        VideoURI = videoURI;
        VideoAddedTimeStamp = videoAddedTimeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public String getVideoURI() {
        return VideoURI;
    }

    public void setVideoURI(String videoURI) {
        VideoURI = videoURI;
    }

    public String getVideoAddedTimeStamp() {
        return VideoAddedTimeStamp;
    }

    public void setVideoAddedTimeStamp(String videoAddedTimeStamp) {
        VideoAddedTimeStamp = videoAddedTimeStamp;
    }
}
