package com.privatepe.app.model.city;

import com.google.gson.annotations.SerializedName;

public class CityResult {
    @SerializedName("id")
    private int id;
    @SerializedName("city")
    private String city;
    @SerializedName("status")
    private int status;
    @SerializedName("isSelected")
    private boolean isSelected;
    public int getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getCity() {
        return city;
    }

    public int getStatus() {
        return status;
    }
}
