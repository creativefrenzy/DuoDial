package com.privatepe.app.adapter.metend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.privatepe.app.R;
import com.privatepe.app.fragments.metend.FriendsFragment;
import com.privatepe.app.fragments.metend.InboxFragment;

import java.util.HashMap;
import java.util.Map;


public class MsgMenuPagerAdapter extends FragmentStatePagerAdapter {
    private final Map<Integer, Fragment> fragmentMap;
    private final String[] tabTitles = new String[]{"Message", "Friends"};
    private final int[] imageResId = {R.drawable.under_msg_un_selected, R.drawable.under_msg_un_selected};
    private final int[] selectedImageResId = {R.drawable.under_msg_selected, R.drawable.under_msg_selected};
    Context context;

    //Constructor to the class
    public MsgMenuPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {

            case 0:
                InboxFragment inboxFragment = new InboxFragment();
                fragmentMap.put(0, inboxFragment);
                return inboxFragment;

            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                fragmentMap.put(1, friendsFragment);
                return friendsFragment;

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
        View v = LayoutInflater.from(context).inflate(R.layout.custom_msg_tab, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        ImageView img = v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);

        return v;
    }

    // I add two func here.
    public void setOnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        ImageView img = view.findViewById(R.id.imgView);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        //textView.setTextSize(18f);
        img.setImageResource(selectedImageResId[position]);

    }

    public void setUnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        ImageView img = view.findViewById(R.id.imgView);
        textView.setTextColor(context.getResources().getColor(R.color.tab_select_txt));
        //textView.setTextSize(15f);
        img.setImageResource(imageResId[position]);
    }
}
