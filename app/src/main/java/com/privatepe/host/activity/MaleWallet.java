package com.privatepe.host.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.privatepe.host.R;
import com.privatepe.host.adapter.TransactionNewAdapter;
import com.privatepe.host.databinding.ActivityMaleWalletBinding;
import com.privatepe.host.response.NewWallet.WalletHistoryDataNew;
import com.privatepe.host.response.NewWallet.WalletResponceNew;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.utils.Constant;
import com.privatepe.host.utils.PaginationScrollListenerLinear;
import com.privatepe.host.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MaleWallet extends AppCompatActivity implements ApiResponseInterface {

    TransactionNewAdapter adapter;
    ActivityMaleWalletBinding binding;
    ApiManager apiManager;
    private int  TOTAL_PAGES, CURRENT_PAGE;
    String start_date = "";
    String end_date = "";
    private Calendar calendar;
    private Boolean ButtonValue = false;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final String from = "From";
    private final String to = "To";
    ArrayList<WalletHistoryDataNew> walletHistoryNew = new ArrayList<>();
    LinearLayoutManager layoutManager;

    boolean rechargeFilter = false;

    private final String ALL = "all";
    private final String TOP_UP = "topup";

    private Boolean filterName = false;
    protected void hideStatusBar(Window window) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        boolean darkText = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkText) {
            flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_male_wallet);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        apiManager.getTransactionHistoryNew(PAGE_START, "", "",ALL);
        adapter = new TransactionNewAdapter(this, walletHistoryNew);
        layoutManager = new LinearLayoutManager(this);
        binding.transactionList.setLayoutManager(layoutManager);
        binding.transactionList.setAdapter(adapter);
        binding.initialDate.setText(from);
        binding.endDate.setText(to);
        pagination();
        dateModification();
        Log.d("UserToken", new SessionManager(this).getUserToken());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void pagination() {

        try {
            binding.transactionList.addOnScrollListener(new PaginationScrollListenerLinear(layoutManager) {

                @Override
                protected void loadMoreItems() {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    isLoading = true;
                    CURRENT_PAGE += 1;
                    new Handler().postDelayed(() -> apiManager.getTransactionHistoryNew(CURRENT_PAGE, start_date, end_date,ALL), 500);

                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

        } catch (Exception e) {

        }

    }

    private void dateModification() {
        binding.initialDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
                ButtonValue = true;

            }
        });


        binding.transactionAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogCall();
            }
        });
        binding.endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
    }

    private void DialogCall() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transaction_filter_bottom_sheet);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.base_bottom_dialog_animation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView all = (TextView) dialog.findViewById(R.id.all);
        TextView recharge = (TextView) dialog.findViewById(R.id.recharge);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletHistoryNew.clear();
                binding.initialDate.setText("From");
                binding.endDate.setText("To");
                binding.suggestionText.setVisibility(View.GONE);
                apiManager.getTransactionHistoryNew(1, "", "",ALL);
                pagination();
                dialog.dismiss();
                nameChange(filterName);



            }
        });
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeFilter = true;
                walletHistoryNew.clear();
                binding.initialDate.setText("From");
                binding.endDate.setText("To");
                binding.suggestionText.setVisibility(View.GONE);
                apiManager.getTransactionHistoryNew(1, "", "",TOP_UP);
                pagination();
                dialog.dismiss();
                filterName=true;
                nameChange(filterName);


            }
        });
    }

    private void nameChange(boolean b) {
        if (b) {
            binding.transactionAllText.setText("TOP UP");
            filterName=false;
        } else {
            binding.transactionAllText.setText("ALL");
        }
    }

    public void showDatePicker(View view) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showDate(year, monthOfYear + 1, dayOfMonth, ButtonValue);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    private void showDate(int year, int month, int day, Boolean Value) {
        if (Value) {
            binding.initialDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
            ButtonValue = false;

        } else {
            binding.endDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));

        }
        start_date = (String) binding.initialDate.getText();
        end_date = (String) binding.endDate.getText();

        if (!(Objects.equals(start_date, from)) && !(Objects.equals(end_date, to))) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date from = format.parse(start_date);
                Date to = format.parse(end_date);
                if(from.after(to)) {
                    walletHistoryNew.clear();
                    binding.suggestionText.setVisibility(View.VISIBLE);
                }else{
                    walletHistoryNew.clear();
                    apiManager.getTransactionHistoryNew(PAGE_START, start_date, end_date,ALL);
                    pagination();
                    binding.suggestionText.setVisibility(View.GONE);
                    progressbar(true);
                    nameChange(filterName);
                }

            } catch (Exception e) {
            }
        }
    }

    private void progressbar(boolean Value) {
        if (Value) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void closeActivity() {
            finish();
        }


    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.TRANSACTION_HISTORY) {
            WalletResponceNew rsp = (WalletResponceNew) response;
            Log.e("wallHistoryResponce","MaleWallet=="+new Gson().toJson(rsp));
            isLoading = false;
            isLastPage = false;

            walletHistoryNew.addAll(rsp.getResult().getWalletHistoryNew());
            adapter.notifyDataSetChanged();
            progressbar(false);
            TOTAL_PAGES = rsp.getResult().getLastPage();
            CURRENT_PAGE = rsp.getResult().getCurrentPage();

            Log.e("cr13", "TOTAL_PAGES " + TOTAL_PAGES);
            Log.e("cr13", "CURRENT_PAGE " + CURRENT_PAGE);
            if (TOTAL_PAGES == CURRENT_PAGE) {
                isLastPage = true;
            }
        }
    }
}
