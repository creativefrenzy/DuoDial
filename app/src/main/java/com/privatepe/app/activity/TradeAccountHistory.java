package com.privatepe.app.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.privatepe.app.R;
import com.privatepe.app.adapter.TradingHistoryRecyclerAdapter;
import com.privatepe.app.databinding.ActivityTradeAccountHistoryBinding;
import com.privatepe.app.response.trading_response.PaginationScrollListener;
import com.privatepe.app.response.trading_response.TradingHistoryResponse;
import com.privatepe.app.response.trading_response.TradingHistoryResult;
import com.privatepe.app.response.trading_response.UpdateTransferDetailResponse;
import com.privatepe.app.retrofit.ApiManager;
import com.privatepe.app.retrofit.ApiResponseInterface;
import com.privatepe.app.utils.BaseActivity;
import com.privatepe.app.utils.Constant;

import java.util.ArrayList;

public class TradeAccountHistory extends BaseActivity implements ApiResponseInterface {

    ActivityTradeAccountHistoryBinding binding;
    TradingHistoryRecyclerAdapter tradingHistoryRecyclerAdapter;

    ArrayList<TradingHistoryResult> tradingHistoryResults = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        // setContentView(R.layout.activity_trade_account_history);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trade_account_history);


        new ApiManager(this, this).getWalletHistoryTradeAccount("1", "10");

        binding.startDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        binding.endDateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutManager = new LinearLayoutManager(this);


        binding.hystoryRecycler.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                Log.e("IsLastPage", "" + isLastPage);
                if (!isLastPage) {
                    binding.loadingProgress.setVisibility(View.VISIBLE);
                } else {
                    binding.loadingProgress.setVisibility(View.GONE);
                }
                isLoading = true;
                currentPage += 1;
                // showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> new ApiManager(TradeAccountHistory.this, TradeAccountHistory.this).getWalletHistoryTradeAccount(String.valueOf(currentPage), "10"), 500);


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

        tradingHistoryRecyclerAdapter = new TradingHistoryRecyclerAdapter(tradingHistoryResults, this, new TradingHistoryRecyclerAdapter.OnButtonClickListener() {
            @Override
            public void onBtuttonClicked(int adapterpos, int btnId, String walletIid) {
                switch (btnId) {
                    case R.id.btn_activate:
                        new ApiManager(TradeAccountHistory.this, TradeAccountHistory.this).updateTransferDetail(walletIid, "activate");
                        tradingHistoryResults.get(adapterpos).setPending(1);
                        tradingHistoryRecyclerAdapter.notifyDataSetChanged();
                        binding.loadingProgress.setVisibility(View.GONE);
                        /*  Toast.makeText(getApplicationContext(), "Activate Button Clicked ", Toast.LENGTH_SHORT).show();
                            Log.e("btnClickked", "" + btnId + "    " + R.id.btn_activate + "   " + "Activate Btn");
                            Log.e("Btn_Ids", "btnId :" + btnId + "    " + "walletId :" + walletId);*/
                        break;
                    case R.id.btn_recall:
                        new ApiManager(TradeAccountHistory.this, TradeAccountHistory.this).updateTransferDetail(walletIid, "recall");
                        tradingHistoryResults.get(adapterpos).setPending(2);
                        tradingHistoryRecyclerAdapter.notifyDataSetChanged();
                        binding.loadingProgress.setVisibility(View.GONE);
                        /* Toast.makeText(getApplicationContext(), "Recall Button Clicked ", Toast.LENGTH_SHORT).show();
                             Log.e("btnClickked", "" + btnId + "    " + R.id.btn_recall + "   " + "Recall Btn");
                             Log.e("Btn_Ids", "btnId :" + btnId + "    " + "walletId :" + walletId);*/
                        break;
                }
            }

        });
        binding.hystoryRecycler.setAdapter(tradingHistoryRecyclerAdapter);

    }


    @Override
    public void isError(String errorCode) {

    }


    @SuppressLint("LongLogTag")
    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.WALLET_HISTORY_TRADE_ACCOUNT) {
            binding.loadingProgress.setVisibility(View.GONE);


            TradingHistoryResponse rsp = (TradingHistoryResponse) response;
            tradingHistoryResults.addAll(rsp.getResult().getData());
            //tradingHistoryRecyclerAdapter.notifyDataSetChanged();
            TOTAL_PAGES = rsp.getResult().getTotal() / rsp.getResult().getPer_page();
            isLoading = false;

            Log.e("lastttPage", "" + currentPage + "  " + TOTAL_PAGES);
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                Log.e("lastttPage", "" + currentPage + "  " + TOTAL_PAGES);

            } else {

            }


            binding.hystoryRecycler.setHasFixedSize(true);

            binding.hystoryRecycler.setLayoutManager(layoutManager);


            //   binding.hystoryRecycler.smoothScrollToPosition(rsp.getResult().getFrom());

            tradingHistoryRecyclerAdapter.notifyDataSetChanged();

        }


        if (ServiceCode == Constant.UPDATE_TRANSFER_DETAIL) {
            UpdateTransferDetailResponse rsp = (UpdateTransferDetailResponse) response;
            Toast.makeText(getApplicationContext(), "" + rsp.getResult(), Toast.LENGTH_SHORT).show();
            Log.e("UpdateTransferDetailResponse", rsp.getResult());


        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}