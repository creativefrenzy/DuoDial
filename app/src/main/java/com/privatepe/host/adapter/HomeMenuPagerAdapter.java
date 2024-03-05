package com.privatepe.host.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.privatepe.host.R;
import com.privatepe.host.fragments.HomeFragment;

import java.util.HashMap;
import java.util.Map;


public class HomeMenuPagerAdapter extends FragmentStatePagerAdapter {
    private final Map<Integer, Fragment> fragmentMap;
   // private final String[] tabTitles = new String[]{/*"Discover",*/ "Popular", "Nearby"};

    private final String[] tabTitles = new String[]{/*"Discover",*/ "Popular"};

    //private final int[] imageResId = {R.drawable.heartattackselected, R.drawable.heartattackunselect};
    //private final int[] selectedImageResId = {R.drawable.heartattackselected, R.drawable.heartattackselected};
    Context context;

    //Constructor to the class
    public HomeMenuPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
           /* case 0:
                DiscoverFragment fragment1 = new DiscoverFragment();
                fragmentMap.put(0, fragment1);
                return fragment1;*/
            case 0:
                HomeFragment fragment2 = new HomeFragment();
                fragmentMap.put(0, fragment2);
                return fragment2;

          // case 1:
          //     NearbyFragment fragment3 = new NearbyFragment();
          //     fragmentMap.put(1, fragment3);
          //     return fragment3;

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
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        ImageView img = v.findViewById(R.id.imgView);
        //img.setImageResource(imageResId[position]);
        return v;
    }

    // I add two func here.
    public void setOnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        ImageView img = view.findViewById(R.id.imgView);
        textView.setTextColor(context.getResources().getColor(R.color.pinkNew));
        textView.setTextSize(20f);
        // img.setImageResource(selectedImageResId[position]);
    }

    public void setUnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        ImageView img = view.findViewById(R.id.imgView);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setTextSize(15f);
        //img.setImageResource(imageResId[position]);
    }
    @Override
    public void restoreState(final Parcelable state, final ClassLoader loader) {
        try {
            super.restoreState(state, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
