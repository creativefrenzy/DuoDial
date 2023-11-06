package com.klive.app.fragments.metend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.klive.app.R;
import com.klive.app.adapter.metend.FriendsMenuPagerAdapter;
import com.klive.app.fragments.BaseFragment;

public class FriendsFragment extends BaseFragment {

    View rootView;
    private TabLayout tabLayout;
    private ViewPager tabViewpager;
    private FriendsMenuPagerAdapter friendsMenuPagerAdapter;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            tabLayout = rootView.findViewById(R.id.tabLayout);
            tabViewpager = rootView.findViewById(R.id.tabViewpager);
        }
        return rootView;
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        friendsMenuPagerAdapter = new FriendsMenuPagerAdapter(getChildFragmentManager(), context);
        tabViewpager.setAdapter(friendsMenuPagerAdapter);
        tabLayout.setupWithViewPager(tabViewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(friendsMenuPagerAdapter.getTabView(i));
        }
        friendsMenuPagerAdapter.setOnSelectView(tabLayout, 0);

    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();

    }

    @Override
    protected void initListners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                friendsMenuPagerAdapter.setOnSelectView(tabLayout, position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                friendsMenuPagerAdapter.setUnSelectView(tabLayout, position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public void onClick(View view) {

    }
}