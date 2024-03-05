package com.privatepe.host.adapter.gift;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.privatepe.host.Interface.GiftSelectListener;
import com.privatepe.host.fragments.gift.GiftTabFragment;
import com.privatepe.host.response.newgiftresponse.NewGiftResult;

import java.util.ArrayList;
import java.util.List;

public class GiftTabPagerAdapter extends FragmentStatePagerAdapter {
    private List<GiftTabFragment> giftTabFragmentArrayList = new ArrayList<>();
    private ArrayList<NewGiftResult> giftResponseList = new ArrayList<>();
    private GiftSelectListener giftSelectListener;
    int Size = 0;

    public GiftTabPagerAdapter(@NonNull FragmentManager fm, List<GiftTabFragment> giftTabFragmentArrayList, ArrayList<NewGiftResult> giftResponseList, GiftSelectListener listener) {
        super(fm);
        this.giftTabFragmentArrayList = giftTabFragmentArrayList;
        this.giftResponseList = giftResponseList;
        this.giftSelectListener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = giftTabFragmentArrayList.get(position);
        Bundle args = new Bundle();
        args.putSerializable("main_gift_list", giftResponseList);
        args.putSerializable("tab_position", position);
        args.putSerializable("giftSelectListner", giftSelectListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return giftTabFragmentArrayList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return giftResponseList.get(position).getName();
    }


    public void setTotalCount(int size) {
        Size = size;
    }
}
