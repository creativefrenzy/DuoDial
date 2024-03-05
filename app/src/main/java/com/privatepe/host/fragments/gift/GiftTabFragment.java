package com.privatepe.host.fragments.gift;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.privatepe.host.Interface.GiftSelectListener;
import com.privatepe.host.R;
import com.privatepe.host.adapter.gift.GiftTabFragmentPagerInternalAdapter;
import com.privatepe.host.response.newgiftresponse.NewGift;
import com.privatepe.host.response.newgiftresponse.NewGiftResult;

import java.util.ArrayList;
import java.util.List;

public class GiftTabFragment extends Fragment {

    private List<GiftTabFragmentInternal> giftTabFragmentArrayList = new ArrayList<>();
    private ArrayList<NewGiftResult> giftResponseList = new ArrayList<>();

    private ArrayList<NewGift> sublist = new ArrayList<>();

    private List<List<NewGift>> splitLists = new ArrayList<List<NewGift>>();

    GiftSelectListener giftSelectListener;
    int mainTabPos = -1;
    int partisionsize = 8;

    String TAG = "GiftTabFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_tab_fragment, container, false);

        /*
        TextView tabno = view.findViewById(R.id.tab_no);
        tabno.setText("" + getArguments().get("tab_position").toString());
        */

        giftResponseList = (ArrayList<NewGiftResult>) getArguments().getSerializable("main_gift_list");
        giftSelectListener = (GiftSelectListener) getArguments().getSerializable("giftSelectListner");
        mainTabPos = getArguments().getInt("tab_position");

        //tabLayout = view.findViewById(R.id.tab_layout2);
        //viewPager2 = view.findViewById(R.id.viewpager2);

        sublist = new ArrayList<>();
        sublist.addAll(giftResponseList.get(mainTabPos).getGifts());

        Log.e(TAG, "onResponse:  gifttab " + giftResponseList.get(1).getGifts().size() + " sublist  " + sublist.size());

        //  sublist= giftResponseList.get(mainTabPos).getGifts();

        splitLists = splitArrayList(sublist, partisionsize);

        Log.e(TAG, "onCreateView: splitLists " + splitLists.size());


        if (sublist.size() > 8) {
            splitLists.clear();
            splitLists = splitArrayList(sublist, partisionsize);
            giftTabFragmentArrayList.clear();

            Log.e(TAG, "onCreateView: " + splitLists.size());

            for (int i = 0; i < splitLists.size(); i++) {
                giftTabFragmentArrayList.add(new GiftTabFragmentInternal());

                Log.e(TAG, "onCreateView: " + splitLists.size());
                GiftTabFragmentPagerInternalAdapter adapter = new GiftTabFragmentPagerInternalAdapter(this, giftTabFragmentArrayList, giftSelectListener, mainTabPos, splitLists);

                ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
                viewPager.setOffscreenPageLimit(1);
                viewPager.setAdapter(adapter);

                TabLayout into_tab_layout = view.findViewById(R.id.tab_layout2);
                new TabLayoutMediator(into_tab_layout, viewPager, (tab, position) -> tab.setText("")).attach();
            }
        } else {

            Log.e(TAG, "onCreateView: sublistsublist " + sublist.size());
            splitLists.clear();
            splitLists.add(sublist);

            giftTabFragmentArrayList.clear();
            giftTabFragmentArrayList.add(new GiftTabFragmentInternal());

            GiftTabFragmentPagerInternalAdapter adapter = new GiftTabFragmentPagerInternalAdapter(this, giftTabFragmentArrayList, giftSelectListener, mainTabPos, splitLists);

            ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
            viewPager.setOffscreenPageLimit(1);
            viewPager.setAdapter(adapter);

            TabLayout into_tab_layout = view.findViewById(R.id.tab_layout2);
            new TabLayoutMediator(into_tab_layout, viewPager, (tab, position) -> tab.setText("")).attach();
        }

        /*   for (int i = 0; i < splitLists.size(); i++) {

            for (int j = 0; j < splitLists.get(i).size(); j++) {

                giftTabFragmentArrayList.add(new GiftTabFragmentInternal());

            }

            GiftTabFragmentPagerInternalAdapter adapter = new GiftTabFragmentPagerInternalAdapter(this, giftTabFragmentArrayList, giftSelectListener, mainTabPos,splitLists.get(i));
            viewPager2.setAdapter(adapter);
            new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    //  tab.setText("");
                }
            }).attach();
        }
*/
        //viewPager2.setOffscreenPageLimit(1);
       /*      GiftTabFragmentPagerInternalAdapter adapter = new GiftTabFragmentPagerInternalAdapter(this, giftTabFragmentArrayList, giftSelectListener, mainTabPos);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //  tab.setText("");
            }
        }).attach();
*/

        return view;
    }

    public <T> List<List<T>> splitArrayList(List<T> list, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Invalid chunk size: " + chunkSize);
        }
        List<List<T>> chunkList = new ArrayList<>(list.size() / chunkSize);
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunkList.add(list.subList(i, i + chunkSize >= list.size() ? list.size() : i + chunkSize));
        }
        return chunkList;
    }

}
