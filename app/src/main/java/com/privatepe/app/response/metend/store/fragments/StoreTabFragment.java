package com.privatepe.app.response.metend.store.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.privatepe.app.R;
import com.privatepe.app.databinding.FragmentStoreBinding;
import com.privatepe.app.response.metend.store.adapters.CategoryTabAdapter;
import com.privatepe.app.response.metend.store.interfaces.OnRefreshListener;
import com.privatepe.app.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.app.response.metend.store_list.StoreResponse;
import com.privatepe.app.response.metend.store_list.StoreResultModel;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class StoreTabFragment extends Fragment implements OnRefreshListener {

    private FragmentStoreBinding binding;
    private ArrayList<Fragment> categoryFragmentList = new ArrayList<>();

    //  private String[] categoryList = new String[]{"Entry", "Frame", "ChatBubble"};

    private SessionManager sessionManager;
    private OnStoreItemClickListener onStoreItemClickListener;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false);
        Log.e("STORE_TAB_TEST", "StoreTabFragment:  onCreateView  ");

        Log.e("BackgroundCrash_Test", "StoreTabFragment: onCreateView:" );

        if (getArguments() != null) {
            onStoreItemClickListener=getArguments().getParcelable("itemClickListener");
        }

        sessionManager = new SessionManager(getContext());
        setupTabs();
        return binding.getRoot();
    }

    /* private void setupTabs() {
         categoryFragmentList.clear();
         for (int i = 0; i < 3; i++) {
             categoryFragmentList.add(new CategoryTabFragment());
         }
         CategoryTabAdapter adapter = new CategoryTabAdapter(getChildFragmentManager(), categoryFragmentList);
         binding.internalViewPager.setAdapter(adapter);
         binding.internalTabLayout.setupWithViewPager(binding.internalViewPager);
     }*/

    private void setupTabs() {

        StoreResponse storeResponse = sessionManager.getStoreTabList();

        if (storeResponse!=null)
        {
            ArrayList<StoreResultModel> storeResult = (ArrayList<StoreResultModel>) storeResponse.getResult();

            categoryFragmentList.clear();
            for (int i = 0; i < storeResult.size(); i++) {
                categoryFragmentList.add(new CategoryTabFragment());
            }

            CategoryTabAdapter adapter = new CategoryTabAdapter(StoreTabFragment.this, categoryFragmentList, storeResult, onStoreItemClickListener,this,"StoreTab");
            binding.storeInternalViewPager.setAdapter(adapter);

            new TabLayoutMediator(binding.storeInternalTabLayout, binding.storeInternalViewPager, (tab, position) -> {
                tab.setCustomView(R.layout.store_category_custom_tab);
                View customTabView = tab.getCustomView();

                if (customTabView != null) {
                    TextView textView = (TextView) customTabView.findViewById(R.id.tabtext);
                    textView.setText(storeResult.get(position).getName());
                    if (position == 0) {
                        textView.setTextColor(requireContext().getResources().getColor(R.color.store_selected_tab_text_color));
                    } else {
                        textView.setTextColor(requireContext().getResources().getColor(R.color.store_unselected_tab_text_color));
                    }

                }

            }).attach();

            binding.storeInternalTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.e("TAB_SELECTED_", "onTabSelected: ");
                    View customTabView = tab.getCustomView();
                    TextView textView = (TextView) customTabView.findViewById(R.id.tabtext);
                    textView.setTextColor(getContext().getResources().getColor(R.color.store_selected_tab_text_color));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    Log.e("TAB_SELECTED_", "onTabUnselected: ");
                    View customTabView = tab.getCustomView();
                    TextView textView = (TextView) customTabView.findViewById(R.id.tabtext);
                    textView.setTextColor(getContext().getResources().getColor(R.color.store_unselected_tab_text_color));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Log.e("TAB_SELECTED_", "onTabReselected: ");
                }
            });



        }


    }


    @Override
    public void onRefresh(String type) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
