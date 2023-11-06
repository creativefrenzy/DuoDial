package com.klive.app.fragments.metend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.klive.app.R;
import com.klive.app.activity.WeeklyRankActivity;
import com.klive.app.adapter.metend.HomeMenuPagerAdapterMet;
import com.klive.app.fragments.BaseFragment;


public class UserMenuFragmentMet extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager tabViewpager;
    private HomeMenuPagerAdapterMet homeMenuPagerAdapter;
    private ImageView img_rank, img_graph;


    public UserMenuFragmentMet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_blank, container, false);

        View view = inflater.inflate(R.layout.fragment_user_menu_met, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabViewpager = view.findViewById(R.id.tabViewpager);
        img_rank = view.findViewById(R.id.img_rank);

        /*
        img_graph = view.findViewById(R.id.img_graph);
        */

        return view;
    }


    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        homeMenuPagerAdapter = new HomeMenuPagerAdapterMet(getChildFragmentManager(), context);
        tabViewpager.setAdapter(homeMenuPagerAdapter);
        tabLayout.setupWithViewPager(tabViewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(homeMenuPagerAdapter.getTabView(i));
        }
        homeMenuPagerAdapter.setOnSelectView(tabLayout, 0);


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
                homeMenuPagerAdapter.setOnSelectView(tabLayout, position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                homeMenuPagerAdapter.setUnSelectView(tabLayout, position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        img_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(getActivity(), WinnerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
              */

                Intent intent = new Intent(getActivity(), WeeklyRankActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}