package com.privatepe.host.response.NewVideoStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewVideoStatusResult {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("video_name")
    @Expose
    private String video_name;

    @SerializedName("Video_thumbnail")
    @Expose
    private String Video_thumbnail;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_thumbnail() {
        return Video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        Video_thumbnail = video_thumbnail;
    }
}
