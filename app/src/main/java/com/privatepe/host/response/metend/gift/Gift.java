package com.privatepe.host.response.metend.gift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gift {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("image")
    @Expose
    private String giftPhoto;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("gift_name")
    @Expose
    private String gift_name;

    @SerializedName("is_animated")
    @Expose
    private String is_animated;

    @SerializedName("gift_category_id")
    @Expose
    private String gift_category_id;

    @SerializedName("sort_order")
    @Expose
    private String sort_order;

    @SerializedName("animation_file")
    @Expose
    private String animation_file;

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

    public String getGiftPhoto() {
        return giftPhoto;
    }

    public void setGiftPhoto(String giftPhoto) {
        this.giftPhoto = giftPhoto;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getIs_animated() {
        return is_animated;
    }

    public void setIs_animated(String is_animated) {
        this.is_animated = is_animated;
    }

    public String getGift_category_id() {
        return gift_category_id;
    }

    public void setGift_category_id(String gift_category_id) {
        this.gift_category_id = gift_category_id;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getAnimation_file() {
        return animation_file;
    }

    public void setAnimation_file(String animation_file) {
        this.animation_file = animation_file;
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
