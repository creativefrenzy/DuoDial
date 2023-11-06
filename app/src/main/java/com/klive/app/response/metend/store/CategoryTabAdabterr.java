package com.klive.app.response.metend.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class CategoryTabAdabterr extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> categoryFragmentsList;

    public CategoryTabAdabterr(@NonNull FragmentManager fm, ArrayList<Fragment> categoryFragmentsList) {
        super(fm);
        this.categoryFragmentsList = categoryFragmentsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = categoryFragmentsList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return categoryFragmentsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Category";
    }
}
