package com.privatepe.app.model.AppUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateResult {
    @SerializedName("app_version")
    @Expose
    private String app_version;
    @SerializedName("app_apk")
    @Expose
    private String app_apk;

    public String getApp_version() {
        return app_version;
    }

    public String getApp_apk() {
        return app_apk;
    }
}
