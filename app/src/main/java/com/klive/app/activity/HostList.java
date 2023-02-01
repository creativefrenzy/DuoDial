package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.klive.app.R;
import com.klive.app.adapter.HostListAdapter;
import com.klive.app.databinding.ActivityHostListBinding;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.response.UserListResponse;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.Constant;
import com.klive.app.utils.PaginationAdapterCallback;
import com.klive.app.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

public class HostList extends BaseActivity implements ApiResponseInterface, PaginationAdapterCallback {

    ActivityHostListBinding binding;
    List<UserListResponse.Data> list = new ArrayList<>();
    ApiManager apiManager;
    HostListAdapter hostListAdapter;
    GridLayoutManager gridLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    @Override
    public void onCreate(Bundle savedInstanceState) {
       hideStatusBar(getWindow(),true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_host_list);
        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.hostList.setLayoutManager(gridLayoutManager);
        apiManager = new ApiManager(this, this);
        apiManager.getUserLists(String.valueOf(currentPage), "");





        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                apiManager.searchaUserLists(newText);
                return true;
            }
        });
        binding.hostList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                //showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getHostListNextPage(String.valueOf(currentPage)), 500);
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

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.USER_LIST) {
            UserListResponse rsp = (UserListResponse) response;
            list = rsp.getResult().getData();
            TOTAL_PAGES = rsp.getResult().getLastPage();
            Log.e("inHostdata", new Gson().toJson(rsp.getResult()));

            Log.e("inHostdatasize", String.valueOf(list.size()));
            if (list.size() > 0) {
                hostListAdapter = new HostListAdapter(HostList.this, this);
                binding.hostList.setAdapter(hostListAdapter);
                Log.e("HistListData", new Gson().toJson(rsp.getResult()));
                hostListAdapter.addAll(list);
                Log.e("HistListDataList", new Gson().toJson(list));
                if (currentPage < TOTAL_PAGES) {
                    hostListAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
                hostListAdapter.notifyDataSetChanged();
            }
        }
        if (ServiceCode == Constant.HOST_LIST_NEXT_PAGE) {
            UserListResponse rsp = (UserListResponse) response;
            hostListAdapter.removeLoadingFooter();
            isLoading = false;
            List<UserListResponse.Data> results = rsp.getResult().getData();
            list.addAll(results);
            hostListAdapter.addAll(results);
            hostListAdapter.notifyDataSetChanged();
            if (currentPage != TOTAL_PAGES) hostListAdapter.addLoadingFooter();
            else isLastPage = true;
        }

    }

    @Override
    public void retryPageLoad() {
        //apiManager.getHostListNextPage(String.valueOf(currentPage), "");
    }
}