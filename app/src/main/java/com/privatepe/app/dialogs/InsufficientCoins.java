package com.privatepe.app.dialogs;

import static com.privatepe.app.utils.Constant.GET_FIRST_TIME_RECHARGE;
import static com.privatepe.app.utils.Constant.GET_FIRST_TIME_RECHARGE_LIST;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.privatepe.app.R;
import com.privatepe.app.activity.OfflineCoinSellingWebActivity;
import com.privatepe.app.activity.SelectPaymentMethod;
import com.privatepe.app.adapter.metend.CoinPlansAdapter;
import com.privatepe.app.response.metend.DiscountedRecharge.DiscountedRechargeResponse;
import com.privatepe.app.response.metend.FirstTimeRechargeListResponse;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.RecyclerTouchListener;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class InsufficientCoins extends Dialog implements ApiResponseInterface {

    ApiManager apiManager;
    RecyclerView plan_list;
    CoinPlansAdapter coinPlansAdapter;
    List<RechargePlanResponseNew.Data> list = new ArrayList<>();
    List<RechargePlanResponseNew.Data> list_Nri = new ArrayList<>();
    String upiId = "";
    int type, callRate;
    SessionManager sessionManager;
    RechargePlanResponseNew.Data listPosition;
    private CardView cv_offlinebanner;

    public InsufficientCoins(@NonNull Context context, int type, int callRate) {
        super(context);
        this.type = type;
        this.callRate = callRate;
        init();
        Log.e("InsufficientCoins1", "InsufficientCoins: Less Coins ");

        String recValue=new SessionManager(getContext()).getFirstTimeRecharged();
        if (recValue.equals("1")){
            cv_offlinebanner.setVisibility(View.VISIBLE);
        }else {
            cv_offlinebanner.setVisibility(View.GONE);
        }


        cv_offlinebanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BannerTesttt", "onClick: " );
                getContext().startActivity(new Intent(getContext(), OfflineCoinSellingWebActivity.class));
            }
        });


        if (sessionManager.getUserLocation().equals("India")) {


            listPosition = list.get(1);

        } else {
            listPosition = list_Nri.get(1);
        }
    }
    TextView tv_coins,tv_price;
    CardView cd_first_offer;

    void init() {
        try {
            this.setContentView(R.layout.dialog_insufficient_coins);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);
            //   this.setCancelable(false);

            TextView term_condition = findViewById(R.id.term_condition);
            TextView tag_line = findViewById(R.id.tag_line);
            TextView coin_min = findViewById(R.id.coin_min);
            TextView tv_topup = findViewById(R.id.tv_topup);
            tv_coins = findViewById(R.id.tv_coins);
            tv_price = findViewById(R.id.tv_price);
            cd_first_offer = findViewById(R.id.cd_first_offer);

            cv_offlinebanner = findViewById(R.id.cv_offlinebanner);
            coin_min.setText(callRate + "/min");
            if (type == 6) {
                coin_min.setVisibility(View.GONE);
                tag_line.setText("Purchase a plan to enable chat service");
                term_condition.setText("* Recharge to enable 1 to 1 video chat");
            }

            plan_list = findViewById(R.id.plan_list);
            plan_list.setLayoutManager(new GridLayoutManager(getContext(), 2));

            apiManager = new ApiManager(getContext(), this);
            sessionManager = new SessionManager(getContext());

            RechargePlanResponseNew rechargePlanResponseNew = sessionManager.getRechargeListResponse();
            if (rechargePlanResponseNew != null) {
                for (int i = 0; i < rechargePlanResponseNew.getResult().size(); i++) {
                    list.add(rechargePlanResponseNew.getResult().get(i));
                }
            }
            if (sessionManager.getUserLocation().equals("India")) {
                //new code close api apiManager.getRechargeList(); 21/4/21
                coinPlansAdapter = new CoinPlansAdapter(getContext(), list, "dialog");
                plan_list.setAdapter(coinPlansAdapter);
                upiId = "BHARATPE.0100633819@indus";
                // apiManager.getRechargeList();


            } else {
                // apiManager.getRechargeListStripe();
                coinPlansAdapter = new CoinPlansAdapter(getContext(), list_Nri, "dialog");
                plan_list.setAdapter(coinPlansAdapter);
                upiId = "BHARATPE.0100633819@indus";
            }
            show();

            plan_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), plan_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                   /* if (!upiId.isEmpty()) {
                        if (sessionManager.getUserLocation().equals("India")) {
                            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", list.get(position));
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", list_Nri.get(position));
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);
                        }
                        dismiss();
                    }*/

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIs_Selected(false);
                    }

                    list.get(position).setIs_Selected(true);
                    coinPlansAdapter.notifyDataSetChanged();
                    cd_first_offer.setBackgroundResource(R.drawable.rounded_corner_stroke_plan_dailog);

                    if (!upiId.isEmpty()) {
                        if (sessionManager.getUserLocation().equals("India")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("selected_plan", list.get(position));
                                    intent.putExtras(bundle);
                                    intent.putExtra("upi_id", upiId);
                                    getContext().startActivity(intent);
                                    dismiss();
                                }
                            },500);
                            //listPosition = list.get(position);
                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("selected_plan", list_Nri.get(position));
                                    intent.putExtras(bundle);
                                    intent.putExtra("upi_id", upiId);
                                    getContext().startActivity(intent);
                                    dismiss();
                                }
                            },500);
                        }
                    }
                }
                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            tv_topup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!upiId.isEmpty()) {
                        if (sessionManager.getUserLocation().equals("India")) {
                            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", listPosition);
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", listPosition);
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);
                        }
                        dismiss();
                    }
                }
            });
        } catch (Exception e) {
        }
        if(sessionManager.getFirstTimeRecharged()!=null){
            if(sessionManager.getFirstTimeRecharged().equalsIgnoreCase("0")){
                cd_first_offer.setVisibility(View.VISIBLE);
                if(sessionManager.getFirstRechargeOffer()!=null){
                    cd_first_offer.setBackgroundResource(R.drawable.rounded_corner_stroke);
                    RechargePlanResponseNew.Data firstRecharge = sessionManager.getFirstRechargeOffer();
                    listPosition = firstRecharge;
                    tv_coins.setText("" + firstRecharge.getPoints());
                    tv_price.setText("₹" + firstRecharge.getAmount());
                    cd_first_offer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cd_first_offer.setBackgroundResource(R.drawable.rounded_corner_stroke);
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setIs_Selected(false);
                            }
                            coinPlansAdapter.notifyDataSetChanged();
                            cd_first_offer.setClickable(false);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cd_first_offer.setClickable(true);
                                    Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("selected_plan", firstRecharge);
                                    intent.putExtras(bundle);
                                    getContext().startActivity(intent);
                                    dismiss();
                                }
                            },500);

                        }
                    });
                }else {
                    apiManager.getFirstTimeRechargeList();
                }
            }else if(sessionManager.getFirstTimeRecharged().equalsIgnoreCase("1")){
                cd_first_offer.setVisibility(View.GONE);
                if (list.size()>1)
                {
                    list.get(1).setIs_Selected(true);
                }
                coinPlansAdapter.notifyDataSetChanged();
            }

        }else {
            apiManager.checkFirstTimeRechargeDone();
        }


    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.RECHARGE_LIST) {
            try {
               // RechargePlanResponse rsp = (RechargePlanResponse) response;
/*

                upiId = rsp.getResult().getUpi_id();

                List<RechargePlanResponse.Data> planList = rsp.getResult().getData();

                if (planList.size() > 0) {
                    for (int i = 0; i < planList.size(); i++) {
                        if (planList.get(i).getStatus() == 1) {
                            if (planList.get(i).getType() == type || planList.get(i).getType() == 7) {
                                list.add(planList.get(i));
                            }
                        }
                    }
                }
*/


                //hide code here for set recharge value static 21/4/21
                /*coinPlansAdapter = new CoinPlansAdapter(getContext(), list, "dialog");
                plan_list.setAdapter(coinPlansAdapter);*/
            } catch (Exception e) {
            }

        }
        if (ServiceCode == GET_FIRST_TIME_RECHARGE) {
            DiscountedRechargeResponse rsp = (DiscountedRechargeResponse) response;
            if (rsp.getIsRecharge() == 0) {
                cd_first_offer.setVisibility(View.VISIBLE);
                apiManager.getFirstTimeRechargeList();
            } else if (rsp.getIsRecharge() == 1) {
                cd_first_offer.setVisibility(View.GONE);
                if (list.size()>1)
                {
                    list.get(1).setIs_Selected(true);
                }
                coinPlansAdapter.notifyDataSetChanged();
            }
        }
        if (ServiceCode == GET_FIRST_TIME_RECHARGE_LIST) {



            FirstTimeRechargeListResponse firstTimeRechargeListResponse = (FirstTimeRechargeListResponse) response;
            RechargePlanResponseNew.Data firstRecharge = firstTimeRechargeListResponse.getResult();
            //Log.e("FirstTimeRechargeListResponse", "isSuccess: HomeFrag" + new Gson().toJson(firstTimeRechargeListResponse));
            cd_first_offer.setBackgroundResource(R.drawable.rounded_corner_stroke);
            listPosition = firstRecharge;
            tv_coins.setText("" + firstRecharge.getPoints());
            tv_price.setText("₹" + firstRecharge.getAmount());
            cd_first_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cd_first_offer.setBackgroundResource(R.drawable.rounded_corner_stroke);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIs_Selected(false);
                    }
                    coinPlansAdapter.notifyDataSetChanged();
                    cd_first_offer.setClickable(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cd_first_offer.setClickable(true);
                            Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", firstRecharge);
                            intent.putExtras(bundle);
                            getContext().startActivity(intent);
                            dismiss();
                        }
                    },500);

                }
            });
        }
    }
    Handler handler = new Handler();
    private void userRechargeData() {
    /*    RechargePlanResponse.Data list1 = new RechargePlanResponse.Data(1, 199, 1, 400, 1, 7, false);
        list.add(list1);
        list1 = new RechargePlanResponse.Data(2, 399, 1, 850, 2, 7, true);
        list.add(list1);
        list1 = new RechargePlanResponse.Data(3, 999, 1, 2150, 3, 7, false);
        list.add(list1);
        list1 = new RechargePlanResponse.Data(4, 2900, 1, 6200, 4, 7, false);
        list.add(list1);
        list1 = new RechargePlanResponse.Data(5, 4499, 1, 10000, 5, 7, false);
        list.add(list1);
        list1 = new RechargePlanResponse.Data(6, 9980, 1, 23000, 7, 7, false);
        list.add(list1);

        list1 = new RechargePlanResponse.Data(50, 3, 2, 160, 2, 7, false);
        list_Nri.add(list1);
        list1 = new RechargePlanResponse.Data(51, 8, 2, 490, 7, 7, true);
        list_Nri.add(list1);
        list1 = new RechargePlanResponse.Data(52, 17, 2, 1100, 17, 7, false);
        list_Nri.add(list1);
        list1 = new RechargePlanResponse.Data(53, 53, 2, 3200, 20, 7, false);
        list_Nri.add(list1);
        list1 = new RechargePlanResponse.Data(54, 90, 2, 5700, 30, 7, false);
        list_Nri.add(list1);
        coinPlansAdapter.notifyDataSetChanged();*/
    }

}