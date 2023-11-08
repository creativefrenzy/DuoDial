package com.privatepe.app.response.metend.store.interfaces;

import android.os.Parcelable;

import com.privatepe.app.response.metend.store_list.StoreItemModel;


public interface OnStoreItemClickListener extends Parcelable {

    void onStoreItemClick(StoreItemModel storeItemModel, String type);

}
