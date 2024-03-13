package com.privatepe.host.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HostUserPicNew implements Parcelable {
    String image_name;

    public HostUserPicNew() {
        // Empty constructor required by Parcelable
    }

    protected HostUserPicNew(Parcel in) {
        image_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HostUserPicNew> CREATOR = new Creator<HostUserPicNew>() {
        @Override
        public HostUserPicNew createFromParcel(Parcel in) {
            return new HostUserPicNew(in);
        }

        @Override
        public HostUserPicNew[] newArray(int size) {
            return new HostUserPicNew[size];
        }
    };

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

}


