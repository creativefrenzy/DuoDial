package com.privatepe.host.response.newgiftresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewGift {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("gift_category_id")
    @Expose
    private int gift_category_id;

    @SerializedName("gift_type")
    @Expose
    private String gift_type;

    @SerializedName("gift_name")
    @Expose
    private String gift_name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("is_animated")
    @Expose
    private int is_animated;

    @SerializedName("animation_file")
    @Expose
    private String animation_file;

    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("gift_beans")
    @Expose
    private float gift_beans;

    public float getGift_beans() {
        return gift_beans;
    }

    @SerializedName("sort_order")
    @Expose
    private int sort_order;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGift_category_id() {
        return gift_category_id;
    }

    public void setGift_category_id(int gift_category_id) {
        this.gift_category_id = gift_category_id;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIs_animated() {
        return is_animated;
    }

    public void setIs_animated(int is_animated) {
        this.is_animated = is_animated;
    }

    public String getAnimation_file() {
        return animation_file;
    }

    public void setAnimation_file(String animation_file) {
        this.animation_file = animation_file;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
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
