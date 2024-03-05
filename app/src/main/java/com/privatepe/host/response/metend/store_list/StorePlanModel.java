package com.privatepe.host.response.metend.store_list;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorePlanModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("store_id")
    @Expose
    private int store_id;

    @SerializedName("validity_in_days")
    @Expose
    private int validity_in_days;

    @SerializedName("coin")
    @Expose
    private int coin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getValidity_in_days() {
        return validity_in_days;
    }

    public void setValidity_in_days(int validity_in_days) {
        this.validity_in_days = validity_in_days;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    protected StorePlanModel(Parcel in) {
        id = in.readInt();
        store_id = in.readInt();
        validity_in_days = in.readInt();
        coin = in.readInt();
    }

    public static final Creator<StorePlanModel> CREATOR = new Creator<StorePlanModel>() {
        @Override
        public StorePlanModel createFromParcel(Parcel in) {
            return new StorePlanModel(in);
        }

        @Override
        public StorePlanModel[] newArray(int size) {
            return new StorePlanModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(store_id);
        dest.writeInt(validity_in_days);
        dest.writeInt(coin);
    }



}
