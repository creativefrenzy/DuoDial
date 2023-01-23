package com.klive.app.activity;


import static android.view.View.GONE;
import static com.klive.app.main.Home.cardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.klive.app.R;
import com.klive.app.adapter.TransactionAdapter;
import com.klive.app.databinding.IncomeReportActivityBinding;
import com.klive.app.dialogs_agency.BankNameListDialog;
import com.klive.app.fragments.ProfileFragment;
import com.klive.app.model.IncomeReportResponce.IncomeReportFemale;
import com.klive.app.model.NewWallet.WalletHistoryData;
import com.klive.app.model.NewWallet.WalletResponce;
import com.klive.app.model.WalletBalResponse;
import com.klive.app.model.WalletRechargeResponse;
import com.klive.app.model.Walletfilter.WalletfilterResponce;
import com.klive.app.model.account.AccountResponse;
import com.klive.app.retrofit.ApiManager;
import com.klive.app.retrofit.ApiResponseInterface;
import com.klive.app.response.AddAccount.AddAccountResponse;
import com.klive.app.utils.Constant;
import com.klive.app.utils.CustomButton;

import java.util.List;
import java.util.TreeMap;


public class IncomeReportActivity extends AppCompatActivity implements ApiResponseInterface, View.OnTouchListener/*, IOnBackPressed*/ {
    private static String amount = "", account = "", dollar = "";
    TransactionAdapter adapter;
    IncomeReportActivityBinding binding;
    ApiManager apiManager;
    int currentBalance = 0;
    int thresholdLimit = 1000;
    TreeMap<String, List<WalletHistoryData>> walletHistory = new TreeMap<>();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private CustomButton customButton;
    private String currBalance = "";
    private int page = 1;
    private int TOTAL_PAGES;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private String type = "";
    private String blockCharacterSet = "~#^|$%&*!\\/*!@#$%^&*(){}_[]|\\?/<>,.:-'';§£¥.+\\ ";
    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private LinearLayout toast;
    private boolean isDetailEmpty = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.income_report_activity);
        binding.setClickListener(new EventHandler(this));

        if (cardView != null) {
            cardView.setVisibility(GONE);
        }

        binding.heading.setText("Income Report");
        linearLayoutManager = new LinearLayoutManager(this);
        binding.transactionList.setLayoutManager(linearLayoutManager);
        customButton = findViewById(R.id.btn_save_details);
        toast = findViewById(R.id.custom_minimum);
        binding.setClickListener(new EventHandler(this));
        customButton.setEnabled(false);

        InputFilter filter1 = new InputFilter.LengthFilter(10);
        binding.etUpiPhoneNumber.setFilters(new InputFilter[]{filter, filter1});
        binding.etEnterUpi.setOnTouchListener(this);
        binding.etEnterConfirmUpi.setOnTouchListener(this);
        binding.etUserUpiName.setOnTouchListener(this);
        binding.etUpiEmailId.setOnTouchListener(this);
        binding.etUpiPhoneNumber.setOnTouchListener(this);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((binding.rlAccountinfoMAIN).getVisibility() == View.VISIBLE) {
                    Animation slideUp = AnimationUtils.loadAnimation(IncomeReportActivity.this, R.anim.slide_down);
                    binding.rlAccountinfoMAIN.setVisibility(GONE);
                    binding.rlAccountinfoMAIN.setAnimation(slideUp);
                    binding.availableCoins.setText(currBalance);
                } else {
                    Animation slideUp = AnimationUtils.loadAnimation(IncomeReportActivity.this, R.anim.slide_up);
                    binding.rlAccountinfoMAIN.setVisibility(View.VISIBLE);
                    binding.rlAccountinfoMAIN.setAnimation(slideUp);
                }
            }
        });

        binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlAccountinfoMAIN.getVisibility() == View.VISIBLE) {
                    Animation slideUp = AnimationUtils.loadAnimation(IncomeReportActivity.this, R.anim.slide_down);
                    binding.rlAccountinfoMAIN.setVisibility(GONE);
                    binding.rlAccountinfoMAIN.setAnimation(slideUp);
                    binding.availableCoins.setText(currBalance);
                }
            }
        });
        binding.imgCloseMAIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlAccountinfoMAIN.getVisibility() == View.VISIBLE) {
                    Animation slideUp = AnimationUtils.loadAnimation(IncomeReportActivity.this, R.anim.slide_down);
                    binding.rlAccountinfoMAIN.setVisibility(GONE);
                    binding.rlAccountinfoMAIN.setAnimation(slideUp);
                    binding.availableCoins.setText(currBalance);
                }
            }
        });
        binding.btnThisweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiManager.getWalletHistroyFilter("this_week");

            }
        });

        binding.btnLastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiManager.getWalletHistroyFilter("last_week");

            }
        });

        apiManager = new ApiManager(this, this);
        apiManager.getWalletAmount();
        // apiManager.getTransactionHistoryFemale(page);
        //new api for WalletHistoryFemaleNew 15/4/21
        apiManager.getWalletHistoryFemaleNew();

        apiManager.getAddAccountDetail();
        binding.tvPre.setClickable(false);
        binding.tvNxt.setClickable(false);

        binding.tvNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = page + 1;
                apiManager.getTransactionHistoryFemale(page);
            }
        });

        binding.tvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = page - 1;
                apiManager.getTransactionHistoryFemale(page);
            }
        });

        binding.imgHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(IncomeReportActivity.this);
                dialog.setContentView(R.layout.dialog_myaccount);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // dialog.setCancelable(false);
                dialog.show();
            }
        });
        paymentDialog();
        binding.llPaymentmethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                //new PaymentMethod(getContext());
                paymentDialog();
            }
        });
        binding.tvrule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulesDialog();
            }
        });

     /*   binding.llLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IncomeReportActivity.this, LevelActivity.class));
            }
        });*/

        binding.details.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                startActivity(new Intent(IncomeReportActivity.this, PaymentStatusActivity.class));
            }
        });


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.et_enter_upi:
                binding.etEnterUpi.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() > 0) {
                            binding.tvErrorU0.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvErrorU0.setVisibility(View.VISIBLE);
                        }
                    }
                });

                break;
            case R.id.et_enter_confirm_upi:
                binding.etEnterConfirmUpi.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String inter_upi = binding.etEnterUpi.getText().toString();
                        String conf_upi = s.toString();
                        if (s.length() > 0 && inter_upi.length() > 0) {
                            if (!inter_upi.equals(conf_upi)) {
                                // give an error that inter_upi and confirm_upi not match
                                binding.tvErrorU1.setVisibility(View.VISIBLE);
                                binding.tvErrorU1.setText("Upi Didn't Match");
                            } else {
                                binding.tvErrorU1.setVisibility(View.INVISIBLE);
                            }

                        }
                    }
                });

                break;
            case R.id.et_user_upi_address:
                binding.etUserUpiName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() >= 15) {
                            binding.tvErrorU2.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvErrorU2.setVisibility(View.VISIBLE);

                        }

                    }
                });
                break;
            case R.id.et_upi_email_id:
                binding.etUpiEmailId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String email = binding.etUpiEmailId.getText().toString();
                        if (email.matches(emailPattern) && charSequence.length() > 0) {
                            binding.tvErrorU3.setText("valid email");
                        } else {
                            binding.tvErrorU3.setText("invalid email");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (txt.matches(emailPattern) && s.length() >= 10) {
                            binding.tvErrorU3.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvErrorU3.setVisibility(View.VISIBLE);

                        }
                    }
                });
                break;
            case R.id.et_upi_phone_number:
                binding.etUpiPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        if (charSequence.length() >= 10) {
                            binding.tvErrorU4.setText("valid Phone number");
                        } else {
                            binding.tvErrorU4.setText("invalid Phone number");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() >= 10) {
                            binding.tvErrorU4.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvErrorU4.setVisibility(View.VISIBLE);
                        }

                    }
                });
                break;
            default:
                break;
        }

        return false;
    }

    public void onResume() {
        super.onResume();
        if (cardView != null) {
            cardView.setVisibility(GONE);
        }

    }

    void paymentDialog() {
        Dialog dialog = new Dialog(IncomeReportActivity.this);
        dialog.setContentView(R.layout.payment_method_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        ImageView back = dialog.findViewById(R.id.img_agency_back);
        RelativeLayout bank = dialog.findViewById(R.id.rl_bank);
        RelativeLayout upi = dialog.findViewById(R.id.rl_upi);
        back.setOnClickListener(view -> {
            dialog.dismiss();

        });
        bank.setOnClickListener(view -> {
            type = "1";
            customButton.setBackground(this.getDrawable(R.drawable.income_bg));
            customButton.setEnabled(true);
            binding.banktype.setVisibility(View.VISIBLE);
            binding.upitype.setVisibility(GONE);
            dialog.dismiss();

        });

        upi.setOnClickListener(view -> {
            // type = "2";
            // customButton.setBackground(this.getDrawable(R.drawable.income_bg));
            // customButton.setEnabled(true);
            // binding.banktype.setVisibility(GONE);
            // binding.upitype.setVisibility(View.VISIBLE);
            // dialog.dismiss();
            Toast.makeText(getApplicationContext(), "UPI coming soon", Toast.LENGTH_SHORT).show();

        });


    }

    private void customToast() {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast_minimum, (ViewGroup) toast);
        Toast toast = new Toast(IncomeReportActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    void requestDialog() {
        Dialog dialog = new Dialog(IncomeReportActivity.this);
        dialog.setContentView(R.layout.request_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        TextView btn = dialog.findViewById(R.id.btn_request);
        ImageView back = dialog.findViewById(R.id.img_agency_back);
        TextView amt, amt1, account;
        amt = dialog.findViewById(R.id.amount);
        amt1 = dialog.findViewById(R.id.amount1);
        account = dialog.findViewById(R.id.account);

        if (type.equals("1")) {
            account.setText(binding.etAccountNumber.getText().toString());
        } else {
            account.setText(binding.etEnterUpi.getText().toString());
        }
        amt.setText("₹" + amount);
        amt1.setText("₹" + amount);
        // RelativeLayout bank = dialog.findViewById(R.id.rl_bank);
        back.setOnClickListener(view -> {
            dialog.dismiss();

        });
        btn.setOnClickListener(view -> {
            Log.e("WithdrawbtnClick", "requestDialog: request withdrawl" );
            btn.setClickable(false);
            btn.setEnabled(false);

            if (type.equals("1")) {
                apiManager.upDateAccountNew(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(), "",
                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                        binding.etPhoneNumber.getText().toString(), "", "1");
                Log.e("IncomeReportActivity", "clickType " + type);

            } else if (type.equals("2")) {
                if (isMandatoryFieldsUpi()) {
                    apiManager.upDateAccountNew(binding.etUserUpiName.getText().toString(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            binding.etUpiPhoneNumber.getText().toString(),
                            binding.etEnterUpi.getText().toString(), "2");
                    Log.e("IncomeReportActivity", "clickType " + type);
                } else {
                    Toast.makeText(IncomeReportActivity.this, "empty", Toast.LENGTH_SHORT).show();
                }
            }

            dialog.dismiss();
            //successDialog();
            //Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
        });
    }

    void successDialog() {
        Dialog dialog = new Dialog(IncomeReportActivity.this);
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView btn = dialog.findViewById(R.id.btn_save_details);
        ImageView back = dialog.findViewById(R.id.img_agency_back);
        // RelativeLayout bank = dialog.findViewById(R.id.rl_bank);
        back.setOnClickListener(view -> {
            dialog.dismiss();
        });
        btn.setOnClickListener(view -> {
            dialog.dismiss();
            // successDialog();
            apiManager.getWalletAmount();
            apiManager.getWalletHistoryFemaleNew();
            apiManager.getAddAccountDetail();
        });
    }

    void rulesDialog() {
        Dialog dialog = new Dialog(IncomeReportActivity.this);
        dialog.setContentView(R.layout.rules_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView btn = dialog.findViewById(R.id.logout);
        btn.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    private boolean isMandatoryFields() {
        if (binding.etUserName.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etUserName " + "empty");
            return false;
        } else if (binding.tvBankCodeInput.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " tvBankCodeInput " + "empty");
            return false;
        } else if (binding.etIfscCode.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etIfscCode " + "empty");
            return false;
        } else if (binding.etAccountNumber.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etAccountNumber " + "empty");
            return false;
        } /*else if (binding.etUserAddress.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etUserAddress "+"empty" );
            return false;
        }*/ /*else if (binding.etEmailId.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etEmailId "+"empty" );
            return false;
        }*/ else if (binding.etPhoneNumber.getText().toString().isEmpty()) {
            Log.e("isMandatoryFields", " etPhoneNumber " + "empty");
            return false;
        }
        Log.e("isMandatoryFields", "nothing empty");
        return true;
    }

    private boolean isMandatoryFieldsUpi() {
        if (binding.etEnterUpi.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etEnterConfirmUpi.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etUserUpiName.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etUpiPhoneNumber.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(IncomeReportActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.UPDATE_ACCOUNT) {
            AccountResponse rsp = (AccountResponse) response;
            Log.e("UpdateData", "UpdateData1 " + rsp.getResult());
            //requestDialog();
            if (rsp.getSuccess()) {
                if (isDetailEmpty) {
                    //  finish();
                    float dCount = Float.parseFloat(dollar);

                    if (type.equals("2")) {
                        if (dCount > 0) {
                            dollar = "0";
                            successDialog();
                        }
                    }
                    if (type.equals("1")) {
                        if (dCount > 0) {
                            dollar = "0";
                            successDialog();
                        }
                    }
                } else {
                    dollar = "0";
                    successDialog();
                }
            } else {
                Toast.makeText(IncomeReportActivity.this, "Something went wrong during connection with server.", Toast.LENGTH_LONG).show();
            }
        }
        if (ServiceCode == Constant.GET_ADD_ACCOUNT_DETAIL) {
            AddAccountResponse rsp = (AddAccountResponse) response;
            Log.e("AccountData", "AccountDataResult1 " + rsp.getResult());
            if (rsp != null && rsp.getResult().size() > 0) {
                binding.etUserName.setText(rsp.getResult().get(0).getAccountName());
                binding.tvBankCodeInput.setText(rsp.getResult().get(0).getBankName());
                binding.etIfscCode.setText(rsp.getResult().get(0).getIfscCode());
                binding.etAccountNumber.setText(rsp.getResult().get(0).getAccountNumber());
                binding.etUserAddress.setText(rsp.getResult().get(0).getAddress());
                binding.etEmailId.setText(rsp.getResult().get(0).getEmail());
                binding.etPhoneNumber.setText(rsp.getResult().get(0).getMobile());

                binding.etEnterUpi.setText(rsp.getResult().get(0).getUpi_id());
                binding.etEnterConfirmUpi.setText(rsp.getResult().get(0).getUpi_id());
                binding.etUserUpiName.setText(rsp.getResult().get(0).getAccountName());
                binding.etUpiEmailId.setText(rsp.getResult().get(0).getEmail());
                binding.etUpiPhoneNumber.setText(rsp.getResult().get(0).getMobile());

                isDetailEmpty = false;
                if (rsp.getResult().get(0).getAccountNumber() == null) {
                    isDetailEmpty = true;
                }
            } else {
                isDetailEmpty = true;
            }

           /* for (int i = 0; i < rsp.getResult().size(); i++) {
                if (rsp.getResult().get(i).getType().equals("1")) {
                    binding.etUserName.setText(rsp.getResult().get(i).getAccountName());
                    binding.tvBankCodeInput.setText(rsp.getResult().get(i).getBankName());
                    binding.etIfscCode.setText(rsp.getResult().get(i).getIfscCode());
                    binding.etAccountNumber.setText(rsp.getResult().get(i).getAccountNumber());
                    binding.etUserAddress.setText(rsp.getResult().get(i).getAddress());
                    binding.etEmailId.setText(rsp.getResult().get(i).getEmail());
                    binding.etPhoneNumber.setText(rsp.getResult().get(i).getMobile());
                    Log.e("AccountData", "AccountData " + rsp.getResult());
                }
            }

            for (int i = 0; i < rsp.getResult().size(); i++) {
                if (rsp.getResult().get(i).getType().equals("2")) {
                    binding.etEnterUpi.setText(rsp.getResult().get(i).getUpi_id());
                    binding.etEnterConfirmUpi.setText(rsp.getResult().get(i).getUpi_id());
                    binding.etUserUpiName.setText(rsp.getResult().get(i).getAccountName());
                    binding.etUpiEmailId.setText(rsp.getResult().get(i).getEmail());
                    binding.etUpiPhoneNumber.setText(rsp.getResult().get(i).getMobile());

                }
            }*/

        }


        if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;

            try {
                currentBalance = rsp.getResult().getRedemablePoints();
                setProgressBar(currentBalance);
            } catch (Exception e) {
            }


        } else if (ServiceCode == Constant.TRANSACTION_HISTORY) {
            WalletResponce rsp = (WalletResponce) response;


            walletHistory = rsp.getResult().getWalletHistory();
            TOTAL_PAGES = ((WalletResponce) response).getResult().getLastPage();


            adapter = new TransactionAdapter(IncomeReportActivity.this, walletHistory.descendingMap());
            binding.transactionList.setAdapter(adapter);

            currBalance = ((WalletResponce) response).getResult().getWalletBalance().getTotalPoint().toString();
            binding.availableCoins.setText(currBalance);
            loading = true;

            if (page == 1) {
                binding.tvPre.setClickable(false);
                binding.tvNxt.setClickable(true);
            } else if (page == TOTAL_PAGES) {
                binding.tvPre.setClickable(true);
                binding.tvNxt.setClickable(false);
            } else {
                binding.tvPre.setClickable(true);
                binding.tvNxt.setClickable(true);
            }

        } else if (ServiceCode == Constant.REDEEM_EARNING) {
            WalletRechargeResponse rsp = (WalletRechargeResponse) response;
            Toast.makeText(IncomeReportActivity.this, "Redeem request sent successfully", Toast.LENGTH_LONG).show();
            apiManager.getWalletAmount();
        } else if (ServiceCode == Constant.FILTER_DATA) {
            WalletfilterResponce rsp = (WalletfilterResponce) response;
            binding.availableCoins.setText(rsp.getResult().getWalletHistory().get(0).getTotalCredited());
            //new  code for incoming report femaleHistoryWallet new 14/4/21
        } else if (ServiceCode == Constant.TRANSACTION_HISTORY_NEW) {
            IncomeReportFemale rsp = (IncomeReportFemale) response;

            amount = "0";
            dollar = "0";

            try {
                amount = rsp.getResult().getAmountInr();
                dollar = rsp.getResult().getAmountDollor();

                binding.tvEarningdata.setText(String.valueOf(rsp.getResult().getRedeemPoint()) + " Beans");

                binding.tvDollardata.setText("$ " + rsp.getResult().getAmountDollor() +
                        " = ₹ " + rsp.getResult().getAmountInr());//â‚¹
            } catch (Exception e) {
            }

           /* WallateResponceFemale rsp = (WallateResponceFemale) response;
            amount = rsp.getResult().getCoinWithIncomeReport().getTotalMoneyInr();
            dollar = rsp.getResult().getCoinWithIncomeReport().getTotalMoneyDollor();
            binding.tvBeandata.setText(rsp.getResult().getCoinWithIncomeReport().getTotalCoins() + " Beans");
            binding.tvDollardata.setText("$ " + rsp.getResult().getCoinWithIncomeReport().getTotalMoneyDollor() +
                    " = ₹ " + rsp.getResult().getCoinWithIncomeReport().getTotalMoneyInr());//â‚¹
            binding.tvEarningdata.setText(String.valueOf(rsp.getResult().getCoinWithIncomeReport().getTotalCoins()) + " Beans");

            binding.tvTodaytotalcoin.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalCoins()));
            binding.tvTodayvideocoin.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalVideoCoins()));
            binding.tvTodayaudiocoin.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalGifts()));

            binding.tvLwtotalcoin.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalCoins()));
            binding.tvLwtotalvideocoin.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalVideoCoins()));
            binding.tvLwtotalaudiocoin.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalGifts()));

            binding.tvInputVideoCoinNaturalToday.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalVideoCoins()));
            binding.tvInputAudioCoinNaturalToday.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalAudioCoins()));
            binding.tvInputGiftCoinNaturalToday.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalGifts()));
            binding.tvInputTotalCoinNaturalToday.setText(String.valueOf(rsp.getResult().getTodayReport().getTotalCoins()));

            binding.tvInputVideoCoinNaturalWeek.setText(String.valueOf(rsp.getResult().getCurrentWeekReport().getTotalVideoCoins()));
            binding.tvInputAudioCoinNaturalWeek.setText(String.valueOf(rsp.getResult().getCurrentWeekReport().getTotalAudioCoins()));
            binding.tvInputGiftCoinNaturalWeek.setText(String.valueOf(rsp.getResult().getCurrentWeekReport().getTotalGifts()));
            binding.tvInputTotalCoinNaturalWeek.setText(String.valueOf(rsp.getResult().getCurrentWeekReport().getTotalCoins()));

            binding.tvInputVideoCoinLastWeek.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalVideoCoins()));
            binding.tvInputAudioCoinLastWeek.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalAudioCoins()));
            binding.tvInputGiftCoinLastWeek.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalGifts()));
            binding.tvInputTotalCoinLastWeek.setText(String.valueOf(rsp.getResult().getLastWeekReport().getTotalCoins()));


            if (rsp.getResult().getAgentId() != null || !rsp.getResult().getAgentId().equals("")) {
                String agencyname = rsp.getResult().getAgentId();
                if (agencyname.length() > 0) {
                    //binding.tvAgencyname.setVisibility(View.VISIBLE);
                    binding.tvAgencyname.setText("Agency name : " + rsp.getResult().getAgentId());
                }
            }*/
        }

    }

    // Set data
    private void setProgressBar(int currentBalance) {
        binding.availableCoins.setText(String.valueOf(currentBalance));
        binding.currentBal.setText(currentBalance + "/" + thresholdLimit);
        int percentage = (int) ((currentBalance / 100.0f) * 10);
        binding.progressPercent.setText(percentage + " %");
        binding.thresholdProgressbar.setProgress(currentBalance);

        if (currentBalance >= thresholdLimit) {
            binding.redeemCoins.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        } else {
            //  binding.redeemCoins.setEnabled(false);
            binding.redeemCoins.setBackgroundColor(getResources().getColor(R.color.grey500));
        }
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void closeActivity() {
            onBackPressed();
            if (cardView != null) {
                cardView.setVisibility(View.VISIBLE);
            }

           /* ProfileFragment fragment2 = new ProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flFragment, fragment2);
            fragmentTransaction.commit();*/
        }

        public void redeemCoins() {
            if (currentBalance >= thresholdLimit) {
                apiManager.redeemCoins(String.valueOf(currentBalance));
            } else {
                Toast.makeText(mContext, "Current coins must be 1000 to redeem", Toast.LENGTH_LONG).show();
            }
        }

        public void backPage() {
            //finish();
        }

        public void chooseBank() {
            BankNameListDialog bankNameListDialog = new BankNameListDialog(mContext);
            bankNameListDialog.show();
            bankNameListDialog.setDialogResult(new BankNameListDialog.OnMyDialogResult() {
                public void finish(String result) {
                    // now you can use the 'result' on your activity
                    Log.e("BankList", "selectedValue " + result);
                    binding.tvBankCodeInput.setText(result);
                }
            });

        }

        /*public void saveData() {
            if (isMandatoryFields()) {
                apiManager.upDateAccount(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(), "",
                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                        binding.etPhoneNumber.getText().toString(), "", "1");
                Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                //finish();
            } else {
                Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
            }

        }*/

        private boolean isWithdrawBtnClicked = false;

        public void saveDetails() {
            Log.e("wallet__", "saveDetails: isWithdrawBtnClicked " + isWithdrawBtnClicked);

            findViewById(R.id.btn_save_details).setClickable(false);
            findViewById(R.id.btn_save_details).setEnabled(false);
            findViewById(R.id.btn_save_details).setBackgroundResource(R.drawable.inactive);

            if (!isWithdrawBtnClicked) {
                if (type.equals("2")) {
                    if (!binding.etEnterUpi.getText().toString().equals(binding.etEnterConfirmUpi.getText().toString())) {
                        Toast.makeText(mContext, "Upi does't match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.e("wallet__", "saveDetails: isWithdrawBtnClicked inside " + isWithdrawBtnClicked);

                float count = Float.parseFloat(dollar);

                if (count == 0) {
                    if (type.equals("1")) {
                        if (isDetailEmpty) {
                        } else {
                            customToast();
                            return;
                        }

                    } else if (type.equals("2")) {
                        if (isDetailEmpty) {
                        } else {
                            customToast();
                            return;
                        }
                    }/* else {
                    customToast();
                    return;
                }*/
                }

                Log.e("wallet__", "saveDetails: Type " + type);
                Log.e("wallet__", "saveDetails: bank " + isMandatoryFields() + "  upi  " + isMandatoryFieldsUpi());
                Log.e("wallet__", "saveDetails: " + binding.etUserName.getText().toString());


                if (isMandatoryFields()) {
                    if (count >= 5) {
                        requestDialog();
                    } else {

                        Log.e("wallet__", "saveDetails: isDetailEmpty " + isDetailEmpty);
                        if (isDetailEmpty) {
                            Log.e("dataEmpty", "p 1");
                            if (type.equals("1")) {
                                apiManager.upDateAccountNew(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(), "",
                                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                                        binding.etPhoneNumber.getText().toString(), "", "1");
                                Log.e("IncomeReportActivity", "clickType " + type);
                                isWithdrawBtnClicked = true;
                                isDetailEmpty = false;
                            } else if (type.equals("2")) {
                                if (isMandatoryFieldsUpi()) {
                                    apiManager.upDateAccountNew(binding.etUserUpiName.getText().toString(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            binding.etUpiPhoneNumber.getText().toString(),
                                            binding.etEnterUpi.getText().toString(), "2");
                                    isWithdrawBtnClicked = true;
                                    Log.e("IncomeReportActivity", "clickType " + type);
                                } else {
                                    Toast.makeText(IncomeReportActivity.this, "empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                            customToast();
                            Log.e("IncomeReportActivity", "saveDetails: customToast1");
                        } else {
                            customToast();
                            Log.e("IncomeReportActivity", "saveDetails: customToast2");
                        }
                    }
                } else if (isMandatoryFieldsUpi()) {
                    if (count >= 5) {
                        requestDialog();
                    } else {
                        if (isDetailEmpty) {
                            Log.e("dataEmpty", "p 2");
                            if (type.equals("1")) {
                                apiManager.upDateAccountNew(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(), "",
                                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                                        binding.etPhoneNumber.getText().toString(), "", "1");
                                Log.e("IncomeReportActivity", "clickType " + type);

                            } else if (type.equals("2")) {
                                if (isMandatoryFieldsUpi()) {
                                    apiManager.upDateAccountNew(binding.etUserUpiName.getText().toString(),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            binding.etUpiPhoneNumber.getText().toString(),
                                            binding.etEnterUpi.getText().toString(), "2");
                                    Log.e("IncomeReportActivity", "clickType " + type);
                                } else {
                                    Toast.makeText(IncomeReportActivity.this, "empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                            customToast();
                        } else {
                            customToast();
                        }
                    }
                    //successDialog();
                    //requestDialog();
                } else {
                    Toast.makeText(mContext, "Form can't be empty", Toast.LENGTH_SHORT).show();
                }

            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isWithdrawBtnClicked = false;
                }
            }, 2000);


        }


        public void pageClose() {
            //dismiss();
        }

        public void add_Account() {
            /*Intent intent = new Intent(mContext, AddAccountActivity.class);
            mContext.startActivity(intent);*/
            //dismiss();
        }

        public void add_Upi() {
            Intent intent = new Intent(mContext, AddUpiActivity.class);
            mContext.startActivity(intent);
            //dismiss();
        }
    }

   /* //@Override
    public void onBackPressed() {
       callParentMethod();
    }
    public void callParentMethod(){
        getActivity().onBackPressed();
    }*/

}