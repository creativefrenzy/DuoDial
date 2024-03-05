package com.privatepe.host.response.metend.store.interfaces;

import android.os.Parcelable;

import com.privatepe.host.response.metend.store_list.StoreItemModel;


public interface OnStoreItemClickListener extends Parcelable {

    void onStoreItemClick(StoreItemModel storeItemModel, String type);

}
