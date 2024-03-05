package com.privatepe.host.adapter.metend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.privatepe.host.R;
import com.privatepe.host.fragments.metend.FollowingFragment;

import java.util.HashMap;
import java.util.Map;


public class FriendsMenuPagerAdapter extends FragmentStatePagerAdapter {
    private final Map<Integer, Fragment> fragmentMap;
    private final String[] tabTitles = new String[]{"Following"/*, "Fans"*/};
    Context context;

    //Constructor to the class
    public FriendsMenuPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {

            case 0:
                FollowingFragment followingFragment = new FollowingFragment();
                fragmentMap.put(0, followingFragment);
                return followingFragment;

            /*case 1:
                FansFragment fansFragment = new FansFragment();
                fragmentMap.put(1, fansFragment);
                return fansFragment;*/

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public Fragment getFragment(int pos) {
        if (fragmentMap == null) return null;
        return fragmentMap.get(pos);
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_follow_tab, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);

        return v;
    }

    // I add two func here.
    public void setOnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        textView.setWidth(R.dimen.DP100);
        //textView.setText(textView.getText().toString()+"-0");
        textView.setBackgroundResource(R.drawable.rounded_follow);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //textView.setTextSize(18f);
    }

    public void setUnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        textView.setWidth(R.dimen.DP100);
        //textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setBackgroundResource(R.drawable.rounded_fans);
       // textView.setText(textView.getText().toString()+"-0");
        textView.setTextColor(context.getResources().getColor(R.color.tab_select_txt));
        //textView.setTextSize(15f);

    }
}
