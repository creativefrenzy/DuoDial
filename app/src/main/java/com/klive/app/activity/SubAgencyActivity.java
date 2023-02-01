package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.klive.app.R;
import com.klive.app.adapter.SubAgencyListAdapter;
import com.klive.app.databinding.ActivitySubAgencyBinding;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.response.sub_agency.SubAgencyData;
import com.klive.app.response.sub_agency.SubAgencyResponse;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.Constant;
import com.klive.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class SubAgencyActivity extends BaseActivity implements ApiResponseInterface, PaginationAdapterCallback {
    ActivitySubAgencyBinding binding;
    List<SubAgencyData> list = new ArrayList<>();
    private ApiManager apiManager;
    SubAgencyListAdapter subAgencyListAdapter;
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
        //setContentView(R.layout.activity_sub_agency);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_agency);
        apiManager = new ApiManager(this, this);
        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.subAgencyList.setLayoutManager(gridLayoutManager);
        binding.setClickListener(new EventHandler(this));
        apiManager.getSubAgencyList("");
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                apiManager.getSubAgencyList(newText);
                return true;
            }
        });

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPage() {
            onBackPressed();
        }

    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.SUB_AGENCY_LIST) {
            SubAgencyResponse rsp = (SubAgencyResponse) response;
            list = rsp.getResult().getData();
            TOTAL_PAGES = rsp.getResult().getLastPage();
            Log.e("inSubAgencydata", new Gson().toJson(rsp.getResult()));
            Log.e("inSubAgencydatalist",""+ list.size());
            if (list.size() > 0) {
                subAgencyListAdapter = new SubAgencyListAdapter(SubAgencyActivity.this, this);
                binding.subAgencyList.setAdapter(subAgencyListAdapter);
                Log.e("inAgency", new Gson().toJson(rsp.getResult()));
                subAgencyListAdapter.addAll(list);
                // Log.e("inuserLevelList", new Gson().toJson(list));
                if (currentPage < TOTAL_PAGES) {
                    subAgencyListAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
                subAgencyListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void retryPageLoad() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}