package com.privatepe.host.adapter.gift;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.privatepe.host.Interface.GiftSelectListener;
import com.privatepe.host.fragments.gift.GiftTabFragmentInternal;
import com.privatepe.host.response.newgiftresponse.NewGift;

import java.util.ArrayList;
import java.util.List;

public class GiftTabFragmentPagerInternalAdapter extends FragmentStateAdapter {

    List<GiftTabFragmentInternal> giftTabFragmentArrayList = new ArrayList<>();
    private GiftSelectListener giftSelectListener;
    private int mainTabPos;
    private List<List<NewGift>> subGiftList;

    public GiftTabFragmentPagerInternalAdapter(@NonNull Fragment fragment, List<GiftTabFragmentInternal> giftTabFragmentArraylist, GiftSelectListener listener, int mainTabPos, List<List<NewGift>> subgiftlist) {
        super(fragment);
        giftTabFragmentArrayList = giftTabFragmentArraylist;
        giftSelectListener = listener;
        this.mainTabPos = mainTabPos;
        subGiftList = subgiftlist;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = giftTabFragmentArrayList.get(position);
        Bundle args = new Bundle();
      // args.putSerializable("main_gift_response_list", giftResponseList);
        args.putInt("main_TabPos", mainTabPos);
        args.putInt("tab_position", position);
        args.putSerializable("giftSelectListner", giftSelectListener);
        args.putSerializable("sub_gift_list", new ArrayList<NewGift>(subGiftList.get(position)));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return giftTabFragmentArrayList.size();
    }
}
