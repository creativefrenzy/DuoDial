package com.privatepe.app.response.metend.store.fragments;


import static com.privatepe.app.utils.Constant.GET_STORE_PURCHASE_TAB_LIST;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.privatepe.app.R;
import com.privatepe.app.databinding.FragmentMineBinding;
import com.privatepe.app.response.metend.store.adapters.CategoryTabAdapter;
import com.privatepe.app.response.metend.store.interfaces.OnRefreshListener;
import com.privatepe.app.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.app.response.metend.store_list.StoreResponse;
import com.privatepe.app.response.metend.store_list.StoreResultModel;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class MineTabFragment extends Fragment implements ApiResponseInterface, OnRefreshListener {

    private FragmentMineBinding binding;

    private ArrayList<Fragment> categoryFragmentList = new ArrayList<>();
    private OnStoreItemClickListener onStoreItemClickListener;

    private String[] categoryList = new String[]{"Entry", "Frame", "ChatBubble"};

    private ApiManager apiManager;
    private SessionManager sessionManager;

    private String TAG = "MineTabFragment";


    private CategoryTabAdapter categoryTabAdapter;

    ArrayList<StoreResultModel> storeResult = new ArrayList<>();


    private boolean type=false;

    private int SelectedTabPos=0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
        sessionManager=new SessionManager(getContext());
        apiManager = new ApiManager(getContext(), this);
        if (getArguments() != null) {
           onStoreItemClickListener = getArguments().getParcelable("itemClickListener");
        }
        apiManager.getStorePurchasedMineTabList();
        Log.e("BackgroundCrash_Test", "CategoryTabFragment onCreateView: " );
        //  setupTabs();
        return binding.getRoot();
    }

    private void setupTabs(/*ArrayList<StoreResultModel> storeResult*/) {
        categoryFragmentList.clear();
        for (int i = 0; i < storeResult.size(); i++) {
            categoryFragmentList.add(new CategoryTabFragment());
        }

  /*    if (storeResult.size() > 0) {
            binding.emptyListLayout.setVisibility(View.GONE);
        } else {
            binding.emptyListLayout.setVisibility(View.VISIBLE);
        }
        */


        categoryTabAdapter = new CategoryTabAdapter(MineTabFragment.this, categoryFragmentList, storeResult, onStoreItemClickListener, this, "MineTab");
        binding.mineInternalViewPager.setAdapter(categoryTabAdapter);

        new TabLayoutMediator(binding.mineInternalTabLayout, binding.mineInternalViewPager, (tab, position) -> {
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

        binding.mineInternalTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("TAB_SELECTED_", "onTabSelected: ");
                View customTabView = tab.getCustomView();
                TextView textView = (TextView) customTabView.findViewById(R.id.tabtext);
                textView.setTextColor(getContext().getResources().getColor(R.color.store_selected_tab_text_color));
                Log.e(TAG, "onTabSelected: " );
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



        binding.mineInternalViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
             SelectedTabPos=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });







    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {


        if (ServiceCode == GET_STORE_PURCHASE_TAB_LIST) {
            StoreResponse rsp = (StoreResponse) response;
            ArrayList<StoreResultModel> resultList = (ArrayList<StoreResultModel>) rsp.getResult();
            storeResult.clear();
            storeResult.addAll(resultList);
            setupTabs();
            binding.mineInternalViewPager.setCurrentItem(SelectedTabPos);
            sessionManager.setPurchasedItems(rsp);




        /*    getActivity().runOnUiThread(() -> );*/
          /*  if (!type)
            {
              //
            }
            else {
              //   categoryTabAdapter.notifyDataSetChanged();
            }*/

         //  Intent notifyIntent=new Intent("NOTIFY-INTERNAL-ADAPTER");
         //  notifyIntent.putExtra("action","update");
         //  getContext().sendBroadcast(notifyIntent);



            Log.e(TAG, "isSuccess: " + new Gson().toJson(rsp));
            //  Log.e(TAG, "isSuccess: resultList " + new Gson().toJson(resultList));

        }


    }



    @Override
    public void onRefresh(String type) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateListReceiver, new IntentFilter("UPDATE-PURCHASE-LIST"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(updateListReceiver);

    }

    BroadcastReceiver updateListReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                String action = intent.getStringExtra("action");

                if (action.equals("update")) {
                    apiManager.getStorePurchasedMineTabList();
                }
                if (action.equals("update1")) {
                    Log.e(TAG, "onReceive: update1 ");
                    apiManager.getStorePurchasedMineTabList();
                    type=true;
                 //   binding.mineInternalViewPager.getAdapter().notifyDataSetChanged();

                    // binding.mineInternalViewPager.getAdapter().notifyDataSetChanged();
                    //  getActivity().runOnUiThread(() -> binding.mineInternalViewPager.getAdapter().notifyDataSetChanged());
                    //categoryTabAdapter.notifyDataSetChanged();



                }

            }
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
