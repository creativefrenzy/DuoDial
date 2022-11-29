package com.klive.app.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.klive.app.R;
import com.klive.app.adapter.AgencyPolicyAdapter;
import com.klive.app.databinding.ActivityAgencyPolicyBinding;
import com.klive.app.databinding.ActivityAnchorPolicyBinding;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.BaseActivity;


public class AnchorPolicyActivity extends BaseActivity implements ApiResponseInterface {
    ActivityAnchorPolicyBinding binding;
    LinearLayoutManager linearLayoutManager;
    AgencyPolicyAdapter agencyPolicyAdapter;
    ApiManager apiManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(),true);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anchor_policy);
        binding.setClickListener(new EventHandler(this));

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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}