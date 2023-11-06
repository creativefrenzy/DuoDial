package com.klive.app.response.metend.store.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.klive.app.response.metend.store_list.StoreResultModel;

import java.util.ArrayList;

public class CategoryMineTabAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> categoryFragmentsList;
    private ArrayList<StoreResultModel> storeResultList;
    public CategoryMineTabAdapter(@NonNull Fragment fragment, ArrayList<Fragment> categoryFragmentsList, ArrayList<StoreResultModel> list) {
        super(fragment);
        this.categoryFragmentsList=categoryFragmentsList;
        storeResultList=list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = categoryFragmentsList.get(position);
      //  Log.e("CategoryTabAdatpter2", "createFragment: "+new Gson().toJson(storeResultList.get(position)) );
        Bundle arguments=new Bundle();
        arguments.putInt("tab_pos",position);
        arguments.putString("type","MineTab");
        arguments.putSerializable("internal_tab_list",storeResultList);
        arguments.putSerializable("itemClickListener",null);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return categoryFragmentsList.size();
    }
}
