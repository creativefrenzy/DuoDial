package com.privatepe.app.activity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.privatepe.app.R;
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
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(),false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_report);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvHostIncome.setLayoutManager(linearLayoutManager);

        apiManager.getCallHistory();

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_CALL_DETAIL) {
            CallDetailResponse detailResponse = (CallDetailResponse) response;

            calldetailList.addAll(detailResponse.getResult().getData());
            if(calldetailList != null && calldetailList.size()>0) {
                binding.rvHostIncome.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.GONE);
                callDetailAdapter = new CallDetailAdapter(CallReportActivity.this, calldetailList);
                binding.rvHostIncome.setAdapter(callDetailAdapter);
            }else{
                binding.rvHostIncome.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }

        }

    }
}