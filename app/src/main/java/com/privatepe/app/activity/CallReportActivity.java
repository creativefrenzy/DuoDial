package com.privatepe.app.activity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.privatepe.app.R;
import com.privatepe.app.Zego.CallNotificationDialog;
import com.privatepe.app.adapter.CallDetailAdapter;
import com.privatepe.app.adapter.HostIncomeAdapter;
import com.privatepe.app.databinding.ActivityCallReportBinding;
import com.privatepe.app.response.CallDetailData;
import com.privatepe.app.response.CallDetailResponse;
import com.privatepe.app.response.HostIncomeResponse.AllWeeklyData;
import com.privatepe.app.response.HostIncomeResponse.IncomeResponse;
import com.privatepe.app.response.HostIncomeResponse.ThisWeekData;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.AppLifecycle;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.PaginationScrollListenerLinear;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CallReportActivity extends BaseActivity implements ApiResponseInterface {
    ActivityCallReportBinding binding;
    List<CallDetailData> calldetailList = new ArrayList<>();
    ApiManager apiManager;
    LinearLayoutManager linearLayoutManager;
    CallDetailAdapter callDetailAdapter;
    SessionManager session;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES, CURRENT_PAGE = PAGE_START;
    private boolean isRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_report);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvHostIncome.setLayoutManager(linearLayoutManager);
        callDetailAdapter = new CallDetailAdapter(CallReportActivity.this, calldetailList);
        binding.rvHostIncome.setAdapter(callDetailAdapter);
        apiManager.getCallHistory(String.valueOf(CURRENT_PAGE));
        binding.shimmerBroadLay.startShimmer();
        binding.shimmerBroadLay.setVisibility(View.VISIBLE);
        pagination();
        checkForNetwork();
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiManager.getCallHistory(String.valueOf(CURRENT_PAGE));
                binding.swipeToRefresh.setRefreshing(true);
                checkForNetwork();
            }
        });

        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onRefresh() {

                if (!isLoading) {
                    isRefresh = true;
                    CURRENT_PAGE = 1;
                    isLoading = false;
                    isLastPage = false;
                    apiManager.getCallHistory(String.valueOf(CURRENT_PAGE));
                    binding.swipeToRefresh.setRefreshing(true);
                    checkForNetwork();
                }
            }
        });
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        /*public void backPage() {
            finish();
        }*/

    }

    @Override
    public void isError(String errorCode) {

    }

    private void pagination() {

        try {
            binding.rvHostIncome.addOnScrollListener(new PaginationScrollListenerLinear(linearLayoutManager) {

                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    CURRENT_PAGE += 1;
                    binding.swipeToRefresh.setRefreshing(true);
                    new Handler().postDelayed(() -> apiManager.getCallHistory(String.valueOf(CURRENT_PAGE)), 500);

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Boolean apiLoaded = false;

    private void checkForNetwork() {
        binding.noNetworkLl.setVisibility(View.GONE);
        if (isNetworkConnected()) {
            return;
        }
        binding.swipeToRefresh.setRefreshing(false);

        if (!apiLoaded) {
            binding.shimmerBroadLay.stopShimmer();
            binding.shimmerBroadLay.setVisibility(View.GONE);
            binding.noNetworkLl.setVisibility(View.VISIBLE);
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_CALL_DETAIL) {
            CallDetailResponse detailResponse = (CallDetailResponse) response;

            apiLoaded = true;
            if (isRefresh) {
                if (calldetailList != null && calldetailList.size() > 0)
                    calldetailList.clear();
                isRefresh = false;
            }
            calldetailList.addAll(detailResponse.getResult().getData());
            binding.shimmerBroadLay.stopShimmer();
            binding.shimmerBroadLay.setVisibility(View.GONE);
            binding.noNetworkLl.setVisibility(View.GONE);
            if (calldetailList != null && calldetailList.size() > 0) {
                binding.swipeToRefresh.setRefreshing(false);
                isLoading = false;
                TOTAL_PAGES = Integer.parseInt(String.valueOf(detailResponse.getResult().getLast_page()));
                binding.rvHostIncome.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.GONE);
                callDetailAdapter.notifyDataSetChanged();
                if (CURRENT_PAGE == TOTAL_PAGES) {
                    isLastPage = true;
                }
            } else {
                binding.rvHostIncome.setVisibility(View.GONE);
                binding.shimmerBroadLay.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }

        }

    }
    BroadcastReceiver callGettingBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra("action");
            if (action.equals("callRequest")){
                String callData= intent.getStringExtra("callData");
                String inviteIdIM= intent.getStringExtra("inviteIdIM");
                callNotificationDialog = new CallNotificationDialog(CallReportActivity.this, callData, inviteIdIM);
            }


        }
    };

    CallNotificationDialog callNotificationDialog;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(callGettingBroadcast, new IntentFilter("KAL-CALLBROADCAST"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(callGettingBroadcast);
    }
}