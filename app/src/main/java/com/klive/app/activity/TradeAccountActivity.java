package com.klive.app.activity;

import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.klive.app.Interface.OnConfirmClickListner;
import com.klive.app.R;
import com.klive.app.databinding.ActivityTradeAccountBinding;
import com.klive.app.dialogs.TradingTransferDialog;
import com.klive.app.response.trading_response.GetTradingUserNameResponse;
import com.klive.app.response.trading_response.TradingAccount;
import com.klive.app.response.trading_response.TradingAccountResponse;
import com.klive.app.response.trading_response.TradingTransferModel;
import com.klive.app.response.trading_response.TransferTradeAccountResponse;
import com.klive.app.response.trading_response.UserIdBodyModel;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.utils.BaseActivity;
import com.klive.app.utils.Constant;
import com.klive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class TradeAccountActivity extends BaseActivity implements ApiResponseInterface {

    ActivityTradeAccountBinding binding;

    String transferToUserId, transferAmount, accountType, transferToUserUserName;

    public String ACCOUNT_TYPE_BALANCE = "Balance";
    public String ACCOUNT_TYPE_TRADE = "Trade Account";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar(getWindow(), true);
        //  setContentView(R.layout.activity_trade_account);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trade_account);

      //  new ApiManager(this, this).getTradingAccount();

        if (getIntent() != null) {

            Log.e("getIntentExtra", "" + getIntent().getStringExtra("tradePoints"));

            if (getIntent().getStringExtra("tradePoints") != null) {
                ////
                binding.tradingCoins.setText(getFormatedAmount(Integer.parseInt(getIntent().getStringExtra("tradePoints"))));
            }


        }


        binding.checkboxBalance.setChecked(true);
        accountType = ACCOUNT_TYPE_BALANCE;

        //  binding.userIdEdittext.setText("579235069");
        //  binding.userIdEdittext.setText("139650579");


        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.checkboxBalance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxTradeaccount.setChecked(false);
                accountType = ACCOUNT_TYPE_BALANCE;
            } else {
               /* binding.checkboxBalance.setChecked(true);
                accountType = ACCOUNT_TYPE_TRADE;*/
            }

        });

        binding.checkboxTradeaccount.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                binding.checkboxBalance.setChecked(false);
                accountType = ACCOUNT_TYPE_TRADE;
            } else {
              /*            binding.checkboxBalance.setChecked(true);
                accountType = ACCOUNT_TYPE_TRADE;*/
            }


        });


        binding.trasferButton.setOnClickListener(v -> {

            new ApiManager(TradeAccountActivity.this, TradeAccountActivity.this).getTradingAccount();

            if (binding.userIdEdittext.getText().toString().equals("")) {
                binding.userIdEdittext.setError("Please enter a valid user Id");
                return;
            } else if (binding.amountEdittext.getText().toString().equals("")) {
                binding.amountEdittext.setError("Please enter a valid amount");
                return;
            } else {

                if (accountType.equals(ACCOUNT_TYPE_BALANCE)) {
                    new ApiManager(this, this).getTradingUserName(new UserIdBodyModel(binding.userIdEdittext.getText().toString(), "1"));
                } else if (accountType.equals(ACCOUNT_TYPE_TRADE)) {
                    new ApiManager(this, this).getTradingUserName(new UserIdBodyModel(binding.userIdEdittext.getText().toString(), "2"));

                }

            }

        });


        binding.historyBtn.setOnClickListener(v -> {
            startActivity(new Intent(TradeAccountActivity.this, TradeAccountHistory.class));
            //  finish();
        });


    }

    private void MakeDefaultState() {

        binding.userIdEdittext.setText("");
        binding.amountEdittext.setText("");
        binding.checkboxBalance.setChecked(true);

    }


    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_TRADING_ACCOUNT) {
            TradingAccountResponse rsp = (TradingAccountResponse) response;
            binding.tradingCoins.setText(getFormatedAmount(rsp.getResult().getTotalPonts()));
        }

        if (ServiceCode == Constant.GET_TRADING_USERNAME) {

            GetTradingUserNameResponse tradingUserNameResponse = (GetTradingUserNameResponse) response;


            if (!tradingUserNameResponse.getResult().equals("Invalid User Id")) {

                Log.e("TradinguserName", "" + tradingUserNameResponse.getResult().getName());


                if (tradingUserNameResponse.getSuccess()) {


                    transferToUserId = binding.userIdEdittext.getText().toString();
                    transferAmount = binding.amountEdittext.getText().toString();
                    transferToUserUserName = tradingUserNameResponse.getResult().getName();


                    if (Integer.parseInt(binding.amountEdittext.getText().toString()) <= Integer.parseInt(binding.tradingCoins.getText().toString().replaceAll(",", ""))) {

                        new TradingTransferDialog(TradeAccountActivity.this, transferToUserUserName, transferToUserId, getFormatedAmount(Integer.parseInt(transferAmount)), accountType, new OnConfirmClickListner() {
                            @Override
                            public void onConfirmClicked(String userId, String TransferAmount, String AccountType) {


                                if (accountType.equals(ACCOUNT_TYPE_BALANCE)) {

                                    new ApiManager(TradeAccountActivity.this, TradeAccountActivity.this).sendtransferTradeAccount(new TradingTransferModel(transferToUserId, transferAmount, "1"));
                                    String remainingPoints = String.valueOf(Integer.parseInt(binding.tradingCoins.getText().toString().replaceAll(",", "")) - Integer.parseInt(transferAmount.replaceAll(",", "")));
                                    binding.tradingCoins.setText(remainingPoints);

                                }
                                if (accountType.equals(ACCOUNT_TYPE_TRADE)) {
                                    new ApiManager(TradeAccountActivity.this, TradeAccountActivity.this).sendtransferTradeAccount(new TradingTransferModel(transferToUserId, transferAmount, "2"));
                                    String remainingPoints = String.valueOf(Integer.parseInt(binding.tradingCoins.getText().toString().replaceAll(",", "")) - Integer.parseInt(transferAmount.replaceAll(",", "")));
                                    binding.tradingCoins.setText(remainingPoints);

                                }
                                startActivity(new Intent(TradeAccountActivity.this, TradeAccountHistory.class));

                                //  MakeDefaultState();
                                //  Toast.makeText(getApplicationContext(), "confirm clicked", Toast.LENGTH_SHORT).show();


                            }
                        });


                    } else {

                        Toast.makeText(this, "You don't have enough diamonds", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(getApplicationContext(), "" + tradingUserNameResponse.getError().toString(), Toast.LENGTH_SHORT).show();
                    Log.e("tradingError", tradingUserNameResponse.getError().toString());
                }


            }

            //  Toast.makeText(getApplicationContext(), "" + rsp.getResult(), Toast.LENGTH_SHORT).show();
            //Log.e("rsp")

        }


        if (ServiceCode == Constant.TRANSFER_TRADE_ACCOUNT) {

            TransferTradeAccountResponse tradeAccountResponse = (TransferTradeAccountResponse) response;

            if (tradeAccountResponse.getResult() != null) {
                Toast.makeText(getApplicationContext(), "Amount transferred Successfully", Toast.LENGTH_SHORT).show();
                binding.tradingCoins.setText(tradeAccountResponse.getResult());
            }


        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        new ApiManager(this, this).getTradingAccount();
        MakeDefaultState();
    }
}