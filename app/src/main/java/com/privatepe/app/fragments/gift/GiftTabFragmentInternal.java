package com.privatepe.app.fragments.gift;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.privatepe.app.Interface.GiftSelectListener;
import com.privatepe.app.R;
import com.privatepe.app.adapter.gift.GiftNewRecyclerAdapter;
import com.privatepe.app.response.newgiftresponse.NewGift;

import java.util.ArrayList;

public class GiftTabFragmentInternal extends Fragment {

    private RecyclerView giftRecycler;
    private GridLayoutManager layoutManager;
    private GiftSelectListener giftSelectListener;
    GiftNewRecyclerAdapter giftNewRecyclerAdapter;
    int tabPos = -1, mainTabPos = -1;

    String TAG = "GiftTabFragmentInternal";

    ArrayList<NewGift> subGiftList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gift_tab_fragment_internal, container, false);

        giftSelectListener = (GiftSelectListener) getArguments().getSerializable("giftSelectListner");
        tabPos = getArguments().getInt("tab_position");
        mainTabPos = getArguments().getInt("main_TabPos");
        subGiftList = (ArrayList<NewGift>) getArguments().getSerializable("sub_gift_list");

        giftRecycler = view.findViewById(R.id.gift_recyclerview);

        layoutManager = new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false);
        giftRecycler.setLayoutManager(layoutManager);
        giftNewRecyclerAdapter = new GiftNewRecyclerAdapter(subGiftList, giftSelectListener, mainTabPos, tabPos);
        giftRecycler.setAdapter(giftNewRecyclerAdapter);

        //Log.e(TAG, "onCreateView: MainTab "+mainTabPos+" SubTab "+tabPos);
        // giftSelectListener.OnGiftSelect("yes i am here");
       /*  TextView tabno = view.findViewById(R.id.tab_no);
        tabno.setText("" + getArguments().get("tab_position").toString()); */


        return view;


    }


    public void setTabPositions(int mainTabPos2, int position) {
    }
}
