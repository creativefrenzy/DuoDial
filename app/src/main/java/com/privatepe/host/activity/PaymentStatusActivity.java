package com.privatepe.host.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.privatepe.host.R;
import com.privatepe.host.adapter.PaymentDetailAdapter;
import com.privatepe.host.model.PaymentRequestResponce.PaymentRequestResponce;
import com.privatepe.host.model.PaymentRequestResponce.PaymentRequestResult;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.BaseActivity;
import com.privatepe.host.utils.Constant;

import java.util.ArrayList;

public class PaymentStatusActivity extends BaseActivity implements ApiResponseInterface {

    RecyclerView rv_paymentstatus;
    LinearLayoutManager linearLayoutManager;
    PaymentDetailAdapter paymentStatusAdapter;
    ArrayList<PaymentRequestResult> paymentRequestResultArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(),true);
        setContentView(R.layout.activity_payment_status);

        new ApiManager(this, this).getPaymentRequestDetail();
    }

    public void backFun(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.GET_PAYMENT_REQUEST_DETAIL) {
                PaymentRequestResponce rsp = (PaymentRequestResponce) response;
                paymentRequestResultArrayList.addAll(rsp.getResult());
                Log.e("arrayData", new Gson().toJson(paymentRequestResultArrayList));
                //paymentStatusAdapter.notifyDataSetChanged();

                rv_paymentstatus = findViewById(R.id.rv_paymentstatus);
                linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rv_paymentstatus.setLayoutManager(linearLayoutManager);
                paymentStatusAdapter = new PaymentDetailAdapter(this, paymentRequestResultArrayList);
                rv_paymentstatus.setAdapter(paymentStatusAdapter);
            }
        } catch (Exception e) {
        }
    }
}