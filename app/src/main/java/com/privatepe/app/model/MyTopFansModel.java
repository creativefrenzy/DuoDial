package com.privatepe.app.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTopFansModel{
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("result")
    @Expose
    public MyTopFansResult result;
    @SerializedName("error")
    @Expose
    public Object error;
}



