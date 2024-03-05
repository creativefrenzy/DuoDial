package com.privatepe.host.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.privatepe.host.R;
import com.privatepe.host.adapter.HostIncomeDetailAdapter;
import com.privatepe.host.databinding.ActivityIncomeDetailBinding;
import com.privatepe.host.response.HostIncomeDetail.IncomeDetailResponse;
import com.privatepe.host.response.HostIncomeDetail.IncomeDetailWalletHistory;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class IncomeDetailActivity extends BaseActivity implements ApiResponseInterface {
    ActivityIncomeDetailBinding binding;
    List<IncomeDetailWalletHistory> list = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    HostIncomeDetailAdapter hostIncomeDetailAdapter;
    ApiManager apiManager;
    String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(),false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_income_detail);

        selectedDate = String.valueOf(getIntent().getSerializableExtra("select_date"));
        Log.e("IncomeDetail", "IncomeDetail0 " + selectedDate);
        String[] items1 = selectedDate.split("-");
        String y1 = items1[0];
        String m1 = items1[1];
        String d1 = items1[2];
        int d = Integer.parseInt(d1);
        int m = Integer.parseInt(m1);
        int y = Integer.parseInt(y1);
        String finalDate = d1 + "-" + m1;
        binding.tvDetailDate.setText(finalDate);
        apiManager = new ApiManager(this, this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvIncomeDetail.setLayoutManager(linearLayoutManager);
        binding.setClickListener(new EventHandler(this));
        apiManager.getIncomeDetails(selectedDate);

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
        if (ServiceCode == Constant.INCOME_REPORT_DETAIL) {
            IncomeDetailResponse rsp = (IncomeDetailResponse) response;
            list = rsp.getResult().getWalletHistory();
            Log.e("IncomeDetail", "IncomeDetail1 " + list);
            hostIncomeDetailAdapter = new HostIncomeDetailAdapter(IncomeDetailActivity.this, list);
            binding.rvIncomeDetail.setAdapter(hostIncomeDetailAdapter);
        }
    }
}