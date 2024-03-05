package com.privatepe.host.response.metend.store.interfaces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public interface OnRefreshListener extends Parcelable {
    void onRefresh(String type);

    @Override
    int describeContents();

    @Override
    void writeToParcel(@NonNull Parcel dest, int flags);
}
