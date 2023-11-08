package com.privatepe.app.model;

public class Profile {
    private int img_id;
    private String img_url;
    private String status;

    public Profile(int img_id, String img_url, String status) {

        this.img_id = img_id;
        this.img_url = img_url;
        this.status = status;
    }

    public int getImg_id() {
        return img_id;
    }

    public String getImg_url(){
        return img_url;
    }

    public String getStatus(){
        return status;
    }
}
