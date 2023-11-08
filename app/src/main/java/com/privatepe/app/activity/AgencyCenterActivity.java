package com.privatepe.app.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.privatepe.app.R;
import com.privatepe.app.adapter.AgencyHostWeeklyAdapter;
import com.privatepe.app.databinding.ActivityAgencyCenterBinding;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.response.AgencyDate.AgencyCenterDateResponse;
import com.privatepe.app.response.AgencyDate.AgencyDateResult;
import com.privatepe.app.response.AgencyHostWeekly.AgencyHostWeeklyResponse;
import com.privatepe.app.response.AgencyHostWeekly.GiftByHostsRecord;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;
import com.privatepe.app.utils.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.privatepe.app.utils.SessionManager.NAME;
import static com.privatepe.app.utils.SessionManager.PROFILE_ID;

public class AgencyCenterActivity extends BaseActivity implements ApiResponseInterface {
    ActivityAgencyCenterBinding binding;
    List<AgencyDateResult> list = new ArrayList<>();
    List<GiftByHostsRecord> listWeekly = new ArrayList<>();
    ApiManager apiManager;
    List<String> listDate = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AgencyHostWeeklyAdapter agencyHostWeeklyAdapter;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideStatusBar(getWindow(),false);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //setContentView(R.layout.activity_agency_center);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_agency_center);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.agencyHostWeeklyList.setLayoutManager(linearLayoutManager);

        apiManager.getAgencyDateList();

        HashMap<String, String> data = session.getUserDetails();
        binding.agencyName.setText(data.get(NAME));
        binding.agecnyId.setText("ID : " + data.get(PROFILE_ID));
        binding.tvAnchor.setTextSize(16f);
        binding.tvSubAgency.setTextSize(12f);

        binding.subAgencyTab.tvSubCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                //ClipData clip = ClipData.newPlainText("id", "https://ringlive.in/socialmedia/sub-apply"+data.get(PROFILE_ID));
                ClipData clip = ClipData.newPlainText("id", "https://ringlive.in/socialmedia/sub-apply");
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });


        Glide.with(getApplicationContext()).load(session.getUserProfilepic()).placeholder(R.drawable.default_profile).into(binding.userImage);

    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPage() {
            onBackPressed();
        }

        public void copy() {
            ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("id", "https://ringlive.in/socialmedia/downloads");
            clipboard.setPrimaryClip(clip);
            Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }


        public void linearAnchor() {
            binding.tvAnchor.setTextSize(17f);
            binding.tvSubAgency.setTextSize(15f);
            binding.tvAnchor.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.tvSubAgency.setTextColor(getResources().getColor(R.color.Grey_new));
            binding.linearTabSubAgency.setVisibility(View.GONE);
            binding.scrollViewAnchor.setVisibility(View.VISIBLE);
        }

        public void linearSub() {
            binding.tvSubAgency.setTextSize(17f);
            binding.tvAnchor.setTextSize(15f);
            binding.tvSubAgency.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.tvAnchor.setTextColor(getResources().getColor(R.color.Grey_new));
            // linear_tab_sub_agency scrollView_anchor
            binding.scrollViewAnchor.setVisibility(View.GONE);
            binding.linearTabSubAgency.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_AGENCY_DATE_LIST) {
            AgencyCenterDateResponse rsp = (AgencyCenterDateResponse) response;
           try {
               list = rsp.getResult();
               Log.e("AgencyCenterAc", "AgencyCenter3 " + list);

               for (int i = 0; i < list.size(); i++) {
                   String[] selectedDate = list.get(i).getSettlementCycle().split(" ~ ");
                   String[] start = selectedDate[0].split("\\s+");
                   String[] end = selectedDate[1].split("\\s+");
                   String startDate = start[1] + "." + start[2];
                   String endDate = end[1] + "." + end[2];
                   listDate.add(startDate + " - " + endDate);

               }
               //for get revers data in to list
            /*for (int i = list.size() - 1; i >= 0; i--) {
                String[] selectedDate = list.get(i).getSettlementCycle().split(" ~ ");
                String[] start = selectedDate[0].split("\\s+");
                String[] end = selectedDate[1].split("\\s+");
                String startDate = start[1] + "." + start[2];
                String endDate = end[1] + "." + end[2];
                listDate.add(startDate + " - " + endDate);
            }*/


               ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_date, listDate);
               binding.spinnerDate.setAdapter(adapter);
               binding.spinnerDate.setPopupBackgroundResource(R.color.colorPrimary);
               binding.spinnerDate.setDropDownVerticalOffset(22);

               String updatedDate = list.get(0).getUpdatedAt();
               apiManager.getAgencyWeeklyData(updatedDate);
               Log.e("AgencyCenterAc", "AgencyCenter5 " + updatedDate);
           }catch (Exception e){
               binding.imgArrow.setVisibility(View.GONE);
               binding.spinnerDate.setVisibility(View.GONE);
           }

        }
        if (ServiceCode == Constant.GET_AGENCY_HOST_WEEKLY_DATA) {
            AgencyHostWeeklyResponse rsp = (AgencyHostWeeklyResponse) response;
            listWeekly = rsp.getResult().getGiftByHostsRecord();
            agencyHostWeeklyAdapter = new AgencyHostWeeklyAdapter(AgencyCenterActivity.this, listWeekly);
            binding.agencyHostWeeklyList.setAdapter(agencyHostWeeklyAdapter);
            Log.e("AgencyCenterAc", "AgencyCenter4 " + listWeekly);
            //double agencyAmount = rsp.getResult().getAgencyAmount();
            //Log.e("AgencyCenterAc", "AgencyCenter6 " + agencyAmount);
            //double final_agencyAmount = Math.round(agencyAmount);
            //Log.e("AgencyCenterAc", "AgencyCenter7 " + final_agencyAmount);

            // Anchor Data Set here
            binding.tvHostIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
            binding.tvAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));
            binding.tvSubAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));

            //double total = rsp.getResult().getTotalPayoutDollar() + rsp.getResult().getAgencyAmount() + rsp.getResult().getSubagencyAmount();
            double total =  rsp.getResult().getAgencyAmount() + rsp.getResult().getSubagencyAmount();
            binding.tvTotalInput.setText("$" + String.valueOf(String.format("%.2f", total)));
            Log.e("AgencyCenterAc", "AgencyCentertotal " + total);

            binding.anchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
            binding.tvCommissionRatioInput.setText(String.valueOf(rsp.getResult().getCommissionRatio() + "%"));
            binding.tvTotalAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));

            binding.tvAnchorIncomeDetailTotalInput.setText("Total: $" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));

            //subAgency Data Set Here
            binding.subAgencyTab.tvSubAgencyAnchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyTotalPayoutDollar())));
            binding.subAgencyTab.tvSubAgencyCommissionRatioInput.setText(String.valueOf(rsp.getResult().getSubagencyCommissionRatio() + "%"));
            binding.subAgencyTab.tvTotalSubAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));

            binding.spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    String updatedDateNew = list.get(position).getUpdatedAt();

                    apiManager.getAgencyWeeklyData(updatedDateNew);
                    agencyHostWeeklyAdapter = new AgencyHostWeeklyAdapter(AgencyCenterActivity.this, listWeekly);
                    binding.agencyHostWeeklyList.setAdapter(agencyHostWeeklyAdapter);

                    binding.tvHostIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
                    binding.tvAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));
                    binding.tvSubAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));
                    binding.tvTotalInput.setText("$" + String.valueOf(String.format("%.2f", total)));

                    binding.anchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
                    binding.tvCommissionRatioInput.setText(String.valueOf(rsp.getResult().getCommissionRatio() + "%"));
                    binding.tvTotalAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));

                    binding.tvAnchorIncomeDetailTotalInput.setText("Total: $" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));

                    Log.e("AgencyCenterAc", "AgencyCenter2 " + item);

                    //subAgency Data Set Here
                    binding.subAgencyTab.tvSubAgencyAnchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyTotalPayoutDollar())));
                    binding.subAgencyTab.tvSubAgencyCommissionRatioInput.setText(String.valueOf(rsp.getResult().getSubagencyCommissionRatio() + "%"));
                    binding.subAgencyTab.tvTotalSubAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }


    }
}