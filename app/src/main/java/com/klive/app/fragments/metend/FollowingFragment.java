package com.klive.app.fragments.metend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.klive.app.R;
import com.klive.app.adapter.metend.FollowUserListAdapter;
import com.klive.app.response.metend.AddRemoveFavResponse;
import com.klive.app.response.metend.FollowingDatum;
import com.klive.app.response.metend.FollowingUsers;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.Constant;
import com.klive.app.utils.PaginationAdapterCallback;
import com.klive.app.utils.PaginationScrollListenerLinear;
import com.zego.ve.Log;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment implements ApiResponseInterface, PaginationAdapterCallback { //FollowUserListAdapter.OnClickedOpenPopupListener

    View rootView;
    RecyclerView rvFriendsList;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private ArrayList<FollowingDatum> contactList = new ArrayList<>();
    private FollowUserListAdapter followUserListAdapter;

    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar progressLoader;
    ViewPager viewPager;
    private ApiManager apiManager;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_following, container, false);
            init();
        }
        return rootView;
    }

    private void init() {

        rvFriendsList = rootView.findViewById(R.id.rvFriendsList);
        progressLoader = rootView.findViewById(R.id.loader);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        viewPager = rootView.findViewById(R.id.viewpager_lab);
        apiManager = new ApiManager(getContext(), this);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFriendsList.setLayoutManager(layoutManager);
        rvFriendsList.setAdapter(followUserListAdapter);
        apiManager.getFollowingHostList(currentPage);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("FBA"));
        rvFriendsList.addOnScrollListener(new PaginationScrollListenerLinear(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.e("loadMoreItems==","=======>");
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                //apiManager.getFollowingHostListNext(currentPage);
                new Handler().postDelayed(() -> apiManager.getFollowingHostListNext(currentPage), 500);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                Log.e("onRefresh===","TOTAL_PAGES====>"+TOTAL_PAGES);
                currentPage = 1;
                isLastPage = false;
                contactList.clear();
                new Handler().postDelayed(() -> apiManager.getFollowingHostList(currentPage), 500);
            }
        });

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("addItem")) {
                currentPage = 1;
                contactList.clear();
                apiManager.getFollowingHostList(currentPage);

            }
        }
    };

    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.FOLLOWING_USER_LIST) {

            FollowingUsers followingUsers = (FollowingUsers) response;
            mSwipeRefreshLayout.setRefreshing(false);
            if(followingUsers.isSuccess()){
                //Log.e("custLog",followingUsers.getFollowingUserData().getTotal()+"");
                Log.e("followingUsers===",new Gson().toJson(followingUsers));
                contactList.addAll(followingUsers.getFollowingUserData().getData());

                TOTAL_PAGES = followingUsers.getFollowingUserData().getLast_page();
                Log.e("TOTAL_PAGES====",TOTAL_PAGES+"");
                if (contactList.size() >= 0) {
                    followUserListAdapter = new FollowUserListAdapter(getActivity(), FollowingFragment.this, FollowingFragment.this);
                    // userList.setItemAnimator(new DefaultItemAnimator());
                    rvFriendsList.setAdapter(followUserListAdapter);

                    // Shuffle Data

                    // Collections.shuffle(list);

                    // Set data in adapter
                    followUserListAdapter.addAll(contactList);

                    if (currentPage < TOTAL_PAGES) {
                        followUserListAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                } else {
                    rvFriendsList.setAdapter(followUserListAdapter);

                }
            }


        }

        if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
            FollowingUsers followingUsers = (FollowingUsers) response;
            Log.e("followingUsers=Next=",new Gson().toJson(followingUsers));
            mSwipeRefreshLayout.setRefreshing(false);

            followUserListAdapter.removeLoadingFooter();
            isLoading = false;

            List<FollowingDatum> results = followingUsers.getFollowingUserData().getData();

            // Shuffle Data
            //  Collections.shuffle(results);

            contactList.addAll(results);

            followUserListAdapter.addAll(results);

            if (currentPage != TOTAL_PAGES) followUserListAdapter.addLoadingFooter();
            else isLastPage = true;
        }

        if (ServiceCode == Constant.FOLLOWING_HOST) {

            AddRemoveFavResponse addRemoveFavResponse = (AddRemoveFavResponse) response;
            if(addRemoveFavResponse.isSuccess()){
                customErrorToast(addRemoveFavResponse.getResult());
                currentPage = 1;
                isLastPage = false;
                contactList.clear();
                new Handler().postDelayed(() -> apiManager.getFollowingHostList(currentPage), 500);
            }
        }
    }

    private LinearLayout toast;
    private void customErrorToast(String msg) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.unable_to_call_lay, (ViewGroup) toast);
        TextView textView = layout.findViewById(R.id.custom_toast_message);
        textView.setText(msg);
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void retryPageLoad() {

    }

    /*@Override
    public void openPopup(String hostId) {
        Log.e("openPopup==",hostId+"=======>");
        showBottomSheetDialog(hostId);
    }*/



}