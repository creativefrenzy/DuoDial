package com.klive.app.model.VideoStatus;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

public class ResourceVideoModel {

    int id;
    Bitmap thumb;
    List<String> VideoLinkLists;
    String ProfileName;
    Date TimeNDate;

    public ResourceVideoModel(int id, Bitmap thumb, List<String> videoLinkLists, String profileName, Date timeNDate) {
        this.id = id;
        this.thumb = thumb;
        VideoLinkLists = videoLinkLists;
        ProfileName = profileName;
        TimeNDate = timeNDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getThumbPath() {
        return thumb;
    }

    public void setThumbPath(Bitmap thumbPath) {
        this.thumb = thumbPath;
    }

    public List<String> getVideoLinkLists() {
        return VideoLinkLists;
    }

    public void setVideoLinkLists(List<String> videoLinkLists) {
        VideoLinkLists = videoLinkLists;
    }

    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String profileName) {
        ProfileName = profileName;
    }

    public Date getTimeNDate() {
        return TimeNDate;
    }

    public void setTimeNDate(Date timeNDate) {
        TimeNDate = timeNDate;
    }
}
