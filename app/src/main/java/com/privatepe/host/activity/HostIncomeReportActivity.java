package com.privatepe.host.activity;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.privatepe.host.R;
import com.privatepe.host.adapter.HostIncomeAdapter;

import com.privatepe.host.databinding.ActivityHostIncomeReportBinding;
import com.privatepe.host.response.HostIncomeResponse.AllWeeklyData;
import com.privatepe.host.response.HostIncomeResponse.IncomeResponse;
import com.privatepe.host.response.HostIncomeResponse.ThisWeekData;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.privatepe.host.utils.SessionManager.NAME;
import static com.privatepe.host.utils.SessionManager.PROFILE_ID;

public class HostIncomeReportActivity extends BaseActivity implements ApiResponseInterface {
    ActivityHostIncomeReportBinding binding;
    List<ThisWeekData> list = new ArrayList<>();
    List<AllWeeklyData> listWeekly = new ArrayList<>();
    ApiManager apiManager;
    List<String> listDate = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    HostIncomeAdapter hostIncomeAdapter;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(),false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_host_income_report);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvHostIncome.setLayoutManager(linearLayoutManager);

        apiManager.getHostThisWeekIncome();

        HashMap<String, String> data = session.getUserDetails();
        binding.hostName.setText(data.get(NAME));
        binding.hostId.setText("ID : " + data.get(PROFILE_ID));

        Glide.with(this).load(new SessionManager(getApplicationContext()).getUserProfilepic()).placeholder(R.drawable.default_profile).into(binding.userImage);

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
        if (ServiceCode == Constant.GET_HOST_THIS_WEEK_DATA) {
            IncomeResponse rsp = (IncomeResponse) response;


            // Anchor Data Set here
           try {
               list = rsp.getResult().getWeeks();
               Log.e("HostIncome", "HostIncomeData1 " + list);
               binding.tvTotalInput.setText(String.valueOf(rsp.getResult().getWeeks().get(0).getTotalCoins()));
               binding.tvVideoCoinInput.setText(String.valueOf(rsp.getResult().getWeeks().get(0).getTotalCallCoins()));
               binding.tvGiftCoinInput.setText(String.valueOf(rsp.getResult().getWeeks().get(0).getTotalGiftCoins()));
               binding.tvDollarInput.setText("$" + String.valueOf(rsp.getResult().getWeeks().get(0).getPayoutDollar()) + "/" + rsp.getResult().getWeeks().get(0).getTotalPayout() + " INR");

               binding.tvRewardCoinInput.setText(String.valueOf(String.valueOf(rsp.getResult().getWeeks().get(0).getTotalRewardCoins())));

               for (int i = 0; i < list.size(); i++) {
                   String[] selectedDate = list.get(i).getSettlementCycle().split(" ~ ");
                   String[] start = selectedDate[0].split("\\s+");
                   String[] end = selectedDate[1].split("\\s+");
                   String startDate = start[1] + "." + start[2];
                   String endDate = end[1] + "." + end[2];
                   listDate.add(startDate + " - " + endDate);
               }

               ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_date, listDate);
               binding.spinnerDate.setAdapter(adapter);
               binding.spinnerDate.setPopupBackgroundResource(R.color.colorPrimary);
               binding.spinnerDate.setDropDownVerticalOffset(22);

               String updatedDate = list.get(0).getUpdatedAt();
               apiManager.getAgencyWeeklyData(updatedDate);
               Log.e("HostIncome", "HostIncomeData2 " + updatedDate);

               binding.spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       Object item = parent.getItemAtPosition(position);

                       String updatedDateNew = list.get(position).getUpdatedAt();
                       Log.e("HostIncomeDate", "HostIncomeData4 " + updatedDateNew);
                       apiManager.getHostAllWeekIncome(updatedDateNew);

                       binding.tvTotalInput.setText(String.valueOf(rsp.getResult().getWeeks().get(position).getTotalCoins()));
                       binding.tvVideoCoinInput.setText(String.valueOf(rsp.getResult().getWeeks().get(position).getTotalCallCoins()));
                       binding.tvGiftCoinInput.setText(String.valueOf(rsp.getResult().getWeeks().get(position).getTotalGiftCoins()));
                       binding.tvRewardCoinInput.setText(String.valueOf(String.valueOf(rsp.getResult().getWeeks().get(position).getTotalRewardCoins())));
                      // int rewardIncome=(int)rsp.getResult().getWeeks().get(position).getTotalCoins()-(int)(Integer.parseInt(rsp.getResult().getWeeks().get(position).getTotalCallCoins())+Integer.parseInt(rsp.getResult().getWeeks().get(position).getTotalGiftCoins()));
                    //   binding.tvRewardCoinInput.setText(String.valueOf(rewardIncome));
                       binding.tvDollarInput.setText("$" + String.valueOf(rsp.getResult().getWeeks().get(position).getPayoutDollar()) + "/" + rsp.getResult().getWeeks().get(position).getTotalPayout() + " INR");
                   }

                   public void onNothingSelected(AdapterView<?> parent) {
                   }
               });
           }catch (Exception e){
               binding.spinnerDate.setVisibility(View.GONE);
               binding.imgArrow.setVisibility(View.GONE);
           }

        }
        if (ServiceCode == Constant.GET_HOST_ALL_WEEK_DATA) {
            IncomeResponse rsp = (IncomeResponse) response;
            listWeekly = rsp.getResult().getFooterData();
            hostIncomeAdapter = new HostIncomeAdapter(HostIncomeReportActivity.this, listWeekly);
            binding.rvHostIncome.setAdapter(hostIncomeAdapter);

        }

    }
}