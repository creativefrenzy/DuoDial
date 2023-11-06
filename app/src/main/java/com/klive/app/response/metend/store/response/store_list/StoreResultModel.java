package com.klive.app.response.metend.store.response.store_list;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreResultModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("stores")
    @Expose
    private List<StoreItemModel> stores;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StoreItemModel> getStores() {
        return stores;
    }

    public void setStores(List<StoreItemModel> stores) {
        this.stores = stores;
    }

    protected StoreResultModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        stores = in.createTypedArrayList(StoreItemModel.CREATOR);
    }

    public static final Creator<StoreResultModel> CREATOR = new Creator<StoreResultModel>() {
        @Override
        public StoreResultModel createFromParcel(Parcel in) {
            return new StoreResultModel(in);
        }

        @Override
        public StoreResultModel[] newArray(int size) {
            return new StoreResultModel[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(stores);
    }
}
