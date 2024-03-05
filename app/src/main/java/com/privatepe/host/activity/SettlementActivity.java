package com.privatepe.host.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.privatepe.host.R;
import com.privatepe.host.adapter.SettlementHostWeeklyAdapter;
import com.privatepe.host.databinding.ActivitySettlementBinding;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.response.SettlementCenter.HostSettlementDateResponse;
import com.privatepe.host.response.SettlementCenter.HostSettlementDateResult;
import com.privatepe.host.response.SettlementDate.SettlementGiftByHostRecord;
import com.privatepe.host.response.SettlementDate.SettlementHostWeeklyResponse;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.privatepe.host.utils.SessionManager.NAME;
import static com.privatepe.host.utils.SessionManager.PROFILE_ID;

public class SettlementActivity extends AppCompatActivity implements ApiResponseInterface {
    ActivitySettlementBinding binding;
    List<HostSettlementDateResult> list = new ArrayList<>();
    List<SettlementGiftByHostRecord> listWeekly = new ArrayList<>();
    ApiManager apiManager;
    List<String> listDate = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    SettlementHostWeeklyAdapter settlementHostWeeklyAdapter;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settlement);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        session = new SessionManager(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.settlementHostWeeklyList.setLayoutManager(linearLayoutManager);

        apiManager.getSettlementDateList();

        HashMap<String, String> data = session.getUserDetails();
        binding.settlementAgencyName.setText(data.get(NAME));
        binding.settlementAgecnyId.setText("ID : " + data.get(PROFILE_ID));
        binding.settlementTvAnchor.setTextSize(16f);
        binding.settlementTvSubAgency.setTextSize(12f);

       /* binding.subAgencyTabSettlement.tvSubCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", binding.subAgencyTabSettlement.tvSubAgencyAnchorDLink.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });*/

    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPage() {
            onBackPressed();
        }

       /* public void copySettlement() {
            ClipboardManager clipboard = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("id", binding.tvSettlementAnchorDLink.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }*/


        public void linearAnchorSettlement() {
            binding.settlementTvAnchor.setTextSize(17f);
            binding.settlementTvSubAgency.setTextSize(14f);
            binding.settlementTvAnchor.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.settlementTvSubAgency.setTextColor(getResources().getColor(R.color.Grey_new));
            binding.linearTabSubAgency.setVisibility(View.GONE);
            binding.scrollViewAnchor.setVisibility(View.VISIBLE);
        }

        public void linearSubSettlement() {
            binding.settlementTvSubAgency.setTextSize(17f);
            binding.settlementTvAnchor.setTextSize(14f);
            binding.settlementTvSubAgency.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.settlementTvAnchor.setTextColor(getResources().getColor(R.color.Grey_new));
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
        if (ServiceCode == Constant.GET_SETTLEMENT_DATE_LIST) {
            HostSettlementDateResponse rsp = (HostSettlementDateResponse) response;
            list = rsp.getResult();
            Log.e("SCAgencyCenterAc", "SCAgencyCenter3 " + list);

            for (int i = 0; i < list.size(); i++) {
                String[] selectedDate = list.get(i).getSettlementCycle().split(" ~ ");
                String[] start = selectedDate[0].split("\\s+");
                String[] end = selectedDate[1].split("\\s+");
                String startDate = start[1] + "." + start[2];
                String endDate = end[1] + "." + end[2];
                listDate.add(startDate + " - " + endDate);

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_date, listDate);
            binding.settlementSpinnerDate.setAdapter(adapter);
            binding.settlementSpinnerDate.setPopupBackgroundResource(R.color.colorPrimary);
            binding.settlementSpinnerDate.setDropDownVerticalOffset(22);

            String updatedDate = list.get(0).getUpdatedAt();
            apiManager.getSettlementWeeklyData(updatedDate);
            Log.e("SCAgencyCenterAc", "SCAgencyCenter5 " + updatedDate);

        }
        if (ServiceCode == Constant.GET_SETTLEMENT_HOST_WEEKLY_DATA) {
            SettlementHostWeeklyResponse rsp = (SettlementHostWeeklyResponse) response;
            listWeekly = rsp.getResult().getGiftByHostsRecord();
            settlementHostWeeklyAdapter = new SettlementHostWeeklyAdapter(SettlementActivity.this, listWeekly);
            binding.settlementHostWeeklyList.setAdapter(settlementHostWeeklyAdapter);
            Log.e("SCAgencyCenterAc", "SCAgencyCenter4 " + listWeekly);
            //double agencyAmount = rsp.getResult().getAgencyAmount();
            //Log.e("SCAgencyCenterAc", "SCAgencyCenter6 " + agencyAmount);
            //double final_agencyAmount = Math.round(agencyAmount);
            //Log.e("SCAgencyCenterAc", "SCAgencyCenter7 " + final_agencyAmount);


            // Anchor Data Set here tv_settlement_anchor_income_detail_total
            binding.settlementTvHostIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
            binding.settlementTvAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));
            binding.settlementTvSubAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));

            double total = rsp.getResult().getTotalPayoutDollar() + rsp.getResult().getAgencyAmount() + rsp.getResult().getSubagencyAmount();
            binding.settlementTvTotalInput.setText("$" + String.valueOf(String.format("%.2f", total)));
            Log.e("SCAgencyCenterAc", "SCAgencyCentertotal " + total);

            binding.tvAnchorSettlementIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
            binding.tvSettlementCommissionRatioInput.setText(String.valueOf(rsp.getResult().getCommissionRatio() + "%"));
            binding.tvSettlementTotalAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));

            binding.tvSettlementAnchorIncomeDetailTotal.setText("Total: $" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));

            // Settlement subAgency Data Set Here
            binding.subAgencyTabSettlement.tvSubAgencyAnchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyTotalPayoutDollar())));
            binding.subAgencyTabSettlement.tvSubAgencyCommissionRatioInput.setText(String.valueOf(rsp.getResult().getSubagencyCommissionRatio() + "%"));
            binding.subAgencyTabSettlement.tvTotalSubAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));

            binding.settlementSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    String updatedDateNew = list.get(position).getUpdatedAt();

                    apiManager.getSettlementWeeklyData(updatedDateNew);
                    settlementHostWeeklyAdapter = new SettlementHostWeeklyAdapter(SettlementActivity.this, listWeekly);
                    binding.settlementHostWeeklyList.setAdapter(settlementHostWeeklyAdapter);

                    binding.settlementTvHostIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
                    binding.settlementTvAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));
                    binding.settlementTvSubAgencyIncomeInput.setText(String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));
                    binding.settlementTvTotalInput.setText("$" + String.valueOf(String.format("%.2f", total)));

                    binding.tvAnchorSettlementIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
                    binding.tvSettlementCommissionRatioInput.setText(String.valueOf(rsp.getResult().getCommissionRatio() + "%"));
                    binding.tvSettlementTotalAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getAgencyAmount())));

                    binding.tvSettlementAnchorIncomeDetailTotal.setText("Total: $" + String.valueOf(String.format("%.2f", rsp.getResult().getTotalPayoutDollar())));
                    Log.e("SCAgencyCenterAc", "SCAgencyCenter2 " + item);

                    // Settlement subAgency Data Set Here
                    binding.subAgencyTabSettlement.tvSubAgencyAnchorIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyTotalPayoutDollar())));
                    binding.subAgencyTabSettlement.tvSubAgencyCommissionRatioInput.setText(String.valueOf(rsp.getResult().getSubagencyCommissionRatio() + "%"));
                    binding.subAgencyTabSettlement.tvTotalSubAgencyIncomeInput.setText("$" + String.valueOf(String.format("%.2f", rsp.getResult().getSubagencyAmount())));
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }


    }


}




