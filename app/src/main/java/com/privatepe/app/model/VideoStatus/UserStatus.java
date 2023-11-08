package com.privatepe.app.model.VideoStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserStatus {

    @SerializedName("id")
    @Expose
    public int id;


    @SerializedName("demouser_id")
    @Expose
    public int demouser_id;


    @SerializedName("video_url")
    @Expose
    public String video_url;


    @SerializedName("status")
    @Expose
    public int status;


    @SerializedName("created_at")
    @Expose
    public String created_at;

    @SerializedName("updated_at")
    @Expose
    public String updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemouser_id() {
        return demouser_id;
    }

    public void setDemouser_id(int demouser_id) {
        this.demouser_id = demouser_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
