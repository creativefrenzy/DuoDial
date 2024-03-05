package com.privatepe.host.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.privatepe.host.R;
import com.privatepe.host.databinding.ActivityAddUpiBinding;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.response.AddAccount.AddAccountResponse;
import com.privatepe.host.response.UdateAccountResponse;
import com.privatepe.host.utils.Constant;

public class AddUpiActivity extends AppCompatActivity implements ApiResponseInterface, View.OnTouchListener {
    ActivityAddUpiBinding binding;

    ApiManager apiManager;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String blockCharacterSet = "~#^|$%&*!\\/*!@#$%^&*(){}_[]|\\?/<>,.:-'';§£¥.+\\ ";
    boolean doubleBackToExitPressedOnce = false;

    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_upi);
        binding.setClickListener(new EventHandler(this));

        apiManager = new ApiManager(this, this);
        apiManager.getAddAccountDetail();

        InputFilter filter1 = new InputFilter.LengthFilter(10);
        binding.etUpiPhoneNumber.setFilters(new InputFilter[]{filter, filter1});
        //binding.etUpiPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        // binding.etUpiPhoneNumber.setFilters(new InputFilter[]{new InputFilterMinMax("0", "12"), new InputFilter.LengthFilter(2)});


        binding.etEnterUpi.setOnTouchListener(this);
        binding.etEnterConfirmUpi.setOnTouchListener(this);
        binding.etUserUpiAddress.setOnTouchListener(this);
        binding.etUpiEmailId.setOnTouchListener(this);
        binding.etUpiPhoneNumber.setOnTouchListener(this);

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
                            binding.tvError0.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError0.setVisibility(View.VISIBLE);

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
                                binding.tvError1.setVisibility(View.VISIBLE);
                                binding.tvError1.setText("Upi Didn't Match");
                            } else {
                                binding.tvError1.setVisibility(View.INVISIBLE);
                            }

                        }
                    }
                });

                break;
            case R.id.et_user_upi_address:
                binding.etUserUpiAddress.addTextChangedListener(new TextWatcher() {
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
                            binding.tvError2.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError2.setVisibility(View.VISIBLE);

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
                            binding.tvError3.setText("valid email");
                        } else {
                            binding.tvError3.setText("invalid email");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (txt.matches(emailPattern) && s.length() >= 10) {
                            binding.tvError3.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError3.setVisibility(View.VISIBLE);

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
                            binding.tvError4.setText("valid Phone number");
                        } else {
                            binding.tvError4.setText("invalid Phone number");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() >= 10) {
                            binding.tvError4.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError4.setVisibility(View.VISIBLE);
                        }

                    }
                });
                break;
            default:
                break;
        }

        return false;
    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPageUpi() {
            finish();
        }


        public void saveUpiData() {
            if (isMandatoryFields()) {
                apiManager.upDateAccount("", "",
                        "", "", "",
                        binding.etUserUpiAddress.getText().toString(), binding.etUpiEmailId.getText().toString(),
                        binding.etUpiPhoneNumber.getText().toString(), binding.etEnterUpi.getText().toString(), "2");

                Toast.makeText(mContext, "Save", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
            }

        }


        public void saveUpiDetails() {
            if (isMandatoryFields()) {
                apiManager.upDateAccount("", "",
                        "", "", "",
                        binding.etUserUpiAddress.getText().toString(), binding.etUpiEmailId.getText().toString(),
                        binding.etUpiPhoneNumber.getText().toString(), binding.etEnterUpi.getText().toString(), "2");
                Toast.makeText(mContext, "Save", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void isError(String errorCode) {
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_ADD_ACCOUNT_DETAIL) {
            AddAccountResponse rsp = (AddAccountResponse) response;
            for (int i = 0; i < rsp.getResult().size(); i++) {
                if (rsp.getResult().get(i).getType().equals("2")) {
                    binding.etEnterUpi.setText(rsp.getResult().get(i).getUpi_id());
                    binding.etEnterConfirmUpi.setText(rsp.getResult().get(i).getUpi_id());
                    binding.etUserUpiAddress.setText(rsp.getResult().get(i).getAddress());
                    binding.etUpiEmailId.setText(rsp.getResult().get(i).getEmail());
                    binding.etUpiPhoneNumber.setText(rsp.getResult().get(i).getMobile());
                }
                Log.e("AccountData", "AccountData " + rsp.getResult());
            }
        }
        if (ServiceCode == Constant.UPDATE_ACCOUNT) {
            UdateAccountResponse rsp = (UdateAccountResponse) response;
            Log.e("UpdateData", "UpdateData1 " + rsp.getResult());


        }

    }

    private boolean isMandatoryFields() {
        if (binding.etEnterUpi.getText().toString().isEmpty()) {
            binding.tvError0.setVisibility(View.VISIBLE);
            return false;
        } else if (binding.etEnterConfirmUpi.getText().toString().isEmpty()) {
            binding.tvError1.setVisibility(View.VISIBLE);
            return false;
        } else if (binding.etUserUpiAddress.getText().toString().isEmpty()) {
            binding.tvError2.setVisibility(View.VISIBLE);
            return false;
        } else if (binding.etUpiEmailId.getText().toString().isEmpty()) {
            binding.tvError3.setVisibility(View.VISIBLE);
            return false;
        } else if (binding.etUpiPhoneNumber.getText().toString().isEmpty()) {
            binding.tvError4.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "click Back again to go Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

}