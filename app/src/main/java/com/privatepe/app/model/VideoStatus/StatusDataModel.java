package com.privatepe.app.model.VideoStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class StatusDataModel {


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String UserName;

    @SerializedName("status")
    @Expose
    public int totalStatus;

    @SerializedName("userstatus")
    @Expose
    public List<UserStatus> userstatus;

    @SerializedName("created_at")
    public Date created_at;

    @SerializedName("updated_at")
    public Date updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(int totalStatus) {
        this.totalStatus = totalStatus;
    }

    public List<UserStatus> getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(List<UserStatus> userstatus) {
        this.userstatus = userstatus;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
