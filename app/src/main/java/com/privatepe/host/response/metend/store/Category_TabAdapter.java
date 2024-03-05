package com.privatepe.host.response.metend.store;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class Category_TabAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> categoryFragmentsList;
    public Category_TabAdapter(@NonNull Fragment fragment, ArrayList<Fragment> categoryFragmentsList) {
        super(fragment);
        this.categoryFragmentsList=categoryFragmentsList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = categoryFragmentsList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return categoryFragmentsList.size();
    }
}
