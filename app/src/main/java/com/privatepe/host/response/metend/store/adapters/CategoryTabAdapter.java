package com.privatepe.host.response.metend.store.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.privatepe.host.response.metend.store.interfaces.OnRefreshListener;
import com.privatepe.host.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.host.response.metend.store_list.StoreResultModel;

import java.util.ArrayList;

public class CategoryTabAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> categoryFragmentsList;
    private ArrayList<StoreResultModel> storeResultList;
    private OnStoreItemClickListener onStoreItemClickListener;
    private OnRefreshListener onRefreshListener;
    private String type;
    public CategoryTabAdapter(@NonNull Fragment fragment, ArrayList<Fragment> categoryFragmentsList, ArrayList<StoreResultModel> list, OnStoreItemClickListener listener,OnRefreshListener refreslistener,String type) {
        super(fragment);
        this.categoryFragmentsList=categoryFragmentsList;
        storeResultList=list;
        onStoreItemClickListener=listener;
        onRefreshListener=refreslistener;
        this.type=type;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = categoryFragmentsList.get(position);
        Bundle arguments=new Bundle();
        arguments.putInt("tab_pos",position);
        arguments.putString("type",type);

        arguments.putParcelableArrayList("internal_tab_list",storeResultList);
        arguments.putParcelable("itemClickListener",onStoreItemClickListener);
        arguments.putParcelable("refreshListener",onRefreshListener);

        fragment.setArguments(arguments);


        return fragment;
    }

    @Override
    public int getItemCount() {
        return categoryFragmentsList.size();
    }
}
