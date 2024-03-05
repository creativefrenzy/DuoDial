package com.privatepe.host.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.privatepe.host.R;
import com.privatepe.host.adapter.AgencyListAdapter;
import com.privatepe.host.databinding.ActivityAgencyPolicyBinding;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.response.Agency.AgencyPolicyResponse;
import com.privatepe.host.response.Agency.Commission;
import com.privatepe.host.response.Agency.SubagencyCommission;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class AgencyPolicy extends BaseActivity implements ApiResponseInterface {
    ActivityAgencyPolicyBinding binding;
    List<Commission> list = new ArrayList<>();
    List<SubagencyCommission> sublist = new ArrayList<>();
    private ApiManager apiManager;
    AgencyListAdapter agencyListAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(), true);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //setContentView(R.layout.activity_agency_policy);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_agency_policy);
        apiManager = new ApiManager(this, this);
        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.agencyRecyclerview.setLayoutManager(gridLayoutManager);
        binding.setClickListener(new EventHandler(this));
        binding.tvAgency.setTextSize(16f);
        binding.tvSunAgency.setTextSize(12f);
        apiManager.getAgencyList();
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
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_AGENCY_LIST) {
            AgencyPolicyResponse rsp = (AgencyPolicyResponse) response;
            Log.e("AgencyPolicydata", new Gson().toJson(rsp.getResult()));

            if (rsp != null) {
                //list.clear();
                list = rsp.getResult().getCommissionList();
                sublist = rsp.getResult().getSubagencyCommissionList();
                Log.e("Agencydata", new Gson().toJson(list));

                agencyListAdapter = new AgencyListAdapter(AgencyPolicy.this, list, sublist, "agency_policy");
                binding.agencyRecyclerview.setAdapter(agencyListAdapter);
                agencyListAdapter.notifyDataSetChanged();

                Log.e("AgencyPolicydataAfter", new Gson().toJson(list));
                Log.e("AgencyPolicydata", "AgencyPolicydata2 " + new Gson().toJson(list.size()));

                binding.linearAgency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        agencyListAdapter = new AgencyListAdapter(AgencyPolicy.this, list, sublist, "agency_policy");
                        binding.agencyRecyclerview.setAdapter(agencyListAdapter);
                        agencyListAdapter.notifyDataSetChanged();
                        binding.tvAgency.setTextSize(17f);
                        binding.tvAgencyWeeklyIncome.setText("Agency weekly income");
                        binding.tvSunAgency.setTextSize(14f);
                        binding.tvAgency.setTextColor(getResources().getColor(R.color.blue));
                        binding.tvSunAgency.setTextColor(getResources().getColor(R.color.Grey_new));
                    }
                });

                binding.linearSubAgency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agencyListAdapter = new AgencyListAdapter(AgencyPolicy.this, list, sublist, "sub_agency_policy");
                        binding.agencyRecyclerview.setAdapter(agencyListAdapter);
                        agencyListAdapter.notifyDataSetChanged();
                        binding.tvSunAgency.setTextSize(17f);
                        binding.tvAgencyWeeklyIncome.setText("Sub_Agency weekly income");
                        binding.tvAgency.setTextSize(14f);
                        binding.tvSunAgency.setTextColor(getResources().getColor(R.color.blue));
                        binding.tvAgency.setTextColor(getResources().getColor(R.color.Grey_new));

                    }
                });
            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
