package com.privatepe.host.model;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileNewResponse {

    @SerializedName("result")
    String result;
    @SerializedName("success")
    boolean success;
    @SerializedName("data")
    public Data data;

    public boolean getSuccess() {
        return success;
    }

    public String getResult() {
        return result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("user_id")
        Integer user_id;

        @SerializedName("image_name")
        String image_name;

        @SerializedName("is_profile_image")
        Integer is_profile_image;

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public Integer getIs_profile_image() {
            return is_profile_image;
        }

        public void setIs_profile_image(Integer is_profile_image) {
            this.is_profile_image = is_profile_image;
        }
    }
}
