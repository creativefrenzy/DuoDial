package com.privatepe.host.response.metend.store.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.privatepe.host.R;
import com.privatepe.host.databinding.FragmentCategoryBinding;
import com.privatepe.host.response.metend.store.adapters.StoreItemListAdapter;
import com.privatepe.host.response.metend.store.interfaces.OnRefreshListener;
import com.privatepe.host.response.metend.store.interfaces.OnStoreItemClickListener;
import com.privatepe.host.response.metend.store_list.StoreResultModel;

import java.util.ArrayList;

public class CategoryTabFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private StoreItemListAdapter storeItemListAdapter;
    private ArrayList<StoreResultModel> storeTabResultList = new ArrayList<>();
    private OnStoreItemClickListener onStoreItemClickListener;
    private OnRefreshListener onRefreshListener;
    private int internalTabPos;
    private String type;
    private String TAG = "CategoryTabFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);

        if (getArguments() != null) {
            storeTabResultList = getArguments().getParcelableArrayList("internal_tab_list");
            onStoreItemClickListener = (OnStoreItemClickListener) getArguments().getParcelable("itemClickListener");
           onRefreshListener = (OnRefreshListener) getArguments().getParcelable("refreshListener");
            internalTabPos = getArguments().getInt("tab_pos");
            type = getArguments().getString("type");
            //  Log.e(TAG, "onCreateView: onRefreshListener "+onRefreshListener );
        }
        Log.e("BackgroundCrash_Test", "CategoryTabFragment onCreate: ");
        setupAdapter();
        setupRefreshView();

        return binding.getRoot();
    }

    private void setupRefreshView() {

        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshListener.onRefresh(type);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeToRefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });

    }

    private void setupAdapter() {

        if (storeTabResultList.size() > internalTabPos) {
            if (storeTabResultList.get(internalTabPos).getStores().size() > 0) {
                binding.emptyIconLayout.setVisibility(View.GONE);
                Log.e(TAG, "setupAdapter: SIZE>0");
                storeItemListAdapter = new StoreItemListAdapter(getContext(), storeTabResultList.get(internalTabPos), onStoreItemClickListener, type);
                binding.storeItemRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
                binding.storeItemRecycler.setAdapter(storeItemListAdapter);
            } else {
                binding.emptyIconLayout.setVisibility(View.VISIBLE);
                Log.e(TAG, "setupAdapter: SIZE<=0");
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        // getContext().registerReceiver(receiver, new IntentFilter("NOTIFY-INTERNAL-ADAPTER"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     /*   try {
            getContext().unregisterReceiver(receiver);
        } catch (Exception e) {
            // already unregistered
        }*/
    }

 /*   BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                String action = intent.getStringExtra("action");

                if (action.equals("update")) {
                    Log.e("AdapterBugTest", "onReceive: receiver2222 ");
                    storeItemListAdapter.notifyDataSetChanged();
                }

            }
        }
    };
*/

}
