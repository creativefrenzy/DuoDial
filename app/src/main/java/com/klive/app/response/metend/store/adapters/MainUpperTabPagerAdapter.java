package com.klive.app.response.metend.store.adapters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.klive.app.response.metend.store.fragments.MineTabFragment;
import com.klive.app.response.metend.store.fragments.StoreTabFragment;
import com.klive.app.response.metend.store.interfaces.OnStoreItemClickListener;
import com.klive.app.response.metend.store.model.MainTabsModel;

import java.util.ArrayList;

public class MainUpperTabPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<MainTabsModel> fragmentArrayList;
    private OnStoreItemClickListener onStoreItemClickListener;

    public MainUpperTabPagerAdapter(@NonNull FragmentManager fm,ArrayList<MainTabsModel> fragmentArrayList,OnStoreItemClickListener listener) {
        super(fm);
        this.fragmentArrayList=fragmentArrayList;
        onStoreItemClickListener=listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentArrayList.get(position).getFragment();
        Bundle bundle=new Bundle();
        if (fragment instanceof StoreTabFragment) {
            bundle.putString("type","StoreTab");
        }
        else if (fragment instanceof MineTabFragment) {
            bundle.putString("type","MineTab");
        }


         bundle.putParcelable("itemClickListener",onStoreItemClickListener);

        Log.e("BackgroundCrash_Test", "MainUpperTabPagerAdapter getItem: " );
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentArrayList.get(position).getTabName();
    }
}
