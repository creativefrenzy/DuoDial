package com.privatepe.app.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.privatepe.app.R;
import com.privatepe.app.activity.OfflineCoinSellingWebActivity;
import com.privatepe.app.activity.SelectPaymentMethod;
import com.privatepe.app.adapter.metend.CoinPlansAdapterMyaccount;
import com.privatepe.app.response.metend.RechargePlan.RechargePlanResponseNew;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.RecyclerTouchListener;
import com.privatepe.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class InsufficientCoinsMyaccount extends Dialog implements ApiResponseInterface {

    ApiManager apiManager;
    RecyclerView plan_list;
    CoinPlansAdapterMyaccount coinPlansAdapter;
    List<RechargePlanResponseNew.Data> list = new ArrayList<>();
    List<RechargePlanResponseNew.Data> list_Nri = new ArrayList<>();
    String upiId = "";
    int type, callRate;
    long totalCoins;
    
    

    SessionManager sessionManager;
    RechargePlanResponseNew.Data listPosition;
    private CardView cv_offlinebanner;

    public InsufficientCoinsMyaccount(@NonNull Context context, int type, long callRate) {
        super(context);

        this.type = type;
     //   this.callRate = callRate;
        totalCoins=callRate;
        init();
        Log.e("InsufficientCoins1", "InsufficientCoins: MyAccount ");
        Log.e("NEW_RECHARGE_LIST_FROM_SESSION", "init: rechargelist " + new Gson().toJson(sessionManager.getRechargeListResponse()));

        // userRechargeData();


        RechargePlanResponseNew rechargePlanResponseNew = sessionManager.getRechargeListResponse();

        if (rechargePlanResponseNew != null) {
            for (int i = 0; i < rechargePlanResponseNew.getResult().size(); i++) {
             /*   if (rechargePlanResponseNew.getResult().get(i).getId() != 7) {

                }*/
                list.add(rechargePlanResponseNew.getResult().get(i));
            }
            if (list.size()>1)
            {
                list.get(1).setIs_Selected(true);
            }

            coinPlansAdapter.notifyDataSetChanged();
        }



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
        

        Log.e("countryLog", sessionManager.getUserLocation());
        if (sessionManager.getUserLocation().equals("India")) {
            // UpdateRechargeData();
            listPosition = list.get(1);
        } else {
            listPosition = list_Nri.get(1);
        }

    }

    void init() {
        try {
            this.setContentView(R.layout.dialog_insufficient_coins_my_account);
            this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.setCancelable(true);


            TextView term_condition = findViewById(R.id.term_condition);
            TextView tag_line = findViewById(R.id.tag_line);
            TextView coin_min = findViewById(R.id.coin_min);
            TextView my_coin = findViewById(R.id.tv_mycoin);
            TextView emailContact = findViewById(R.id.emailContact);
            TextView tv_topup = findViewById(R.id.tv_topup);

            cv_offlinebanner = findViewById(R.id.cv_offlinebanner);
            
            
            
         //   coin_min.setText(callRate + "/min");
            my_coin.setText("My Coin: " + totalCoins);

            if (type == 6) {
                coin_min.setVisibility(View.GONE);
                tag_line.setText("Purchase a plan to enable chat service");
                term_condition.setText("* Video plan will not be applicable on chat service");
            }

            plan_list = findViewById(R.id.plan_list);
            plan_list.setLayoutManager(new GridLayoutManager(getContext(), 2));

            apiManager = new ApiManager(getContext(), this);
            sessionManager = new SessionManager(getContext());
            Log.e("counteryInDailog", sessionManager.getUserLocation());
            if (sessionManager.getUserLocation().equals("India")) {
                //new code close api apiManager.getRechargeList(); 21/4/21
                coinPlansAdapter = new CoinPlansAdapterMyaccount(getContext(), list, "dialog");
                //       apiManager.getRechargeList();
                //       listPosition=list.get(2);

            } else {
                //    apiManager.getRechargeListStripe();
                coinPlansAdapter = new CoinPlansAdapterMyaccount(getContext(), list_Nri, "dialog");
                //    listPosition=list_Nri.get(2);
            }
            plan_list.setAdapter(coinPlansAdapter);
            upiId = "BHARATPE.0100633819@indus";
            show();

            emailContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));// only email apps should handle this
                   // intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zeepliveofficial@gmail.com"});
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rechargeissue22@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Coin User ID " + new SessionManager(getContext()).getUserId());
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(intent);
                    }
                }

            });

            plan_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), plan_list, new RecyclerTouchListener.ClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view, int position) {

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setIs_Selected(false);
                    }

                    list.get(position).setIs_Selected(true);
                    coinPlansAdapter.notifyDataSetChanged();

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

                    if (!upiId.isEmpty()) {
                        if (sessionManager.getUserLocation().equals("India")) {
                           /* Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", list.get(position));
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);*/

                            listPosition = list.get(position);
                        } else {
                           /* Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", list_Nri.get(position));
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);*/

                            listPosition = list.get(position);

                            // listPosition = list_Nri.get(position);

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

                           /* Intent intent = new Intent(getContext(), SelectPaymentMethod.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selected_plan", listPosition);
                            intent.putExtras(bundle);
                            intent.putExtra("upi_id", upiId);
                            getContext().startActivity(intent);*/
                        }
                       /* Intent intent = new Intent(getContext(), SelectPaymentMethodNew.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("selected_plan", listPosition);
                        intent.putExtras(bundle);
                        intent.putExtra("upi_id", upiId);
                        getContext().startActivity(intent);*/

                        dismiss();
                    }
                }
            });

        } catch (Exception e) {
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
                //RechargePlanResponse rsp = (RechargePlanResponse) response;

             /*   upiId = rsp.getResult().getUpi_id();
                Log.e("InsufficientCoins", "isSuccess: "+upiId );
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

                //hide code here for set recharge value static21/4/21
               /* coinPlansAdapter = new CoinPlansAdapterMyaccount(getContext(), list, "dialog");
                plan_list.setAdapter(coinPlansAdapter);*/
            } catch (Exception e) {
            }

        }
    }

    

    private void userRechargeData() {
/*        RechargePlanResponse.Data list1 = new RechargePlanResponse.Data(1, 199, 1, 400, 1, 7, false);
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