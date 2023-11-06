package com.klive.app.response.metend.store_list;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreItemModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("store_category_id")
    @Expose
    private int store_category_id;

    @SerializedName("store_name")
    @Expose
    private String store_name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("animation_file")
    @Expose
    private String animation_file;

    @SerializedName("sound_file")
    @Expose
    private String sound_file;


    @SerializedName("current_time")
    @Expose
    private long current_time;

    @SerializedName("end_time")
    @Expose
    private long end_time;

    @SerializedName("current_plan_coin")
    @Expose
    private int current_plan_coin;

    @SerializedName("is_using")
    @Expose
    private int is_using;

    @SerializedName("storeplan")
    @Expose
    private List<StorePlanModel> storeplan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_category_id() {
        return store_category_id;
    }

    public void setStore_category_id(int store_category_id) {
        this.store_category_id = store_category_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnimation_file() {
        return animation_file;
    }

    public void setAnimation_file(String animation_file) {
        this.animation_file = animation_file;
    }

    public String getSound_file() {
        return sound_file;
    }

    public void setSound_file(String sound_file) {
        this.sound_file = sound_file;
    }

    public List<StorePlanModel> getStoreplan() {
        return storeplan;
    }

    public void setStoreplan(List<StorePlanModel> storeplan) {
        this.storeplan = storeplan;
    }


    public long getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(long current_time) {
        this.current_time = current_time;
    }


    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getCurrent_plan_coin() {
        return current_plan_coin;
    }

    public void setCurrent_plan_coin(int current_plan_coin) {
        this.current_plan_coin = current_plan_coin;
    }

    public int getIs_using() {
        return is_using;
    }

    public void setIs_using(int is_using) {
        this.is_using = is_using;
    }


    protected StoreItemModel(Parcel in) {
        id = in.readInt();
        store_category_id = in.readInt();
        store_name = in.readString();
        image = in.readString();
        animation_file = in.readString();
        sound_file = in.readString();
        current_time = in.readLong();
        end_time = in.readLong();
        current_plan_coin = in.readInt();
        is_using = in.readInt();
        storeplan = in.createTypedArrayList(StorePlanModel.CREATOR);
    }

    public static final Creator<StoreItemModel> CREATOR = new Creator<StoreItemModel>() {
        @Override
        public StoreItemModel createFromParcel(Parcel in) {
            return new StoreItemModel(in);
        }

        @Override
        public StoreItemModel[] newArray(int size) {
            return new StoreItemModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(store_category_id);
        dest.writeString(store_name);
        dest.writeString(image);
        dest.writeString(animation_file);
        dest.writeString(sound_file);
        dest.writeLong(current_time);
        dest.writeLong(end_time);
        dest.writeInt(current_plan_coin);
        dest.writeInt(is_using);
        dest.writeTypedList(storeplan);
    }
}
