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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.privatepe.host.R;
import com.privatepe.host.databinding.ActivityAddAccountBinding;
import com.privatepe.host.dialogs_agency.BankNameListDialog;
import com.privatepe.host.retrofit.ApiManager;
import com.privatepe.host.retrofit.ApiResponseInterface;
import com.privatepe.host.response.AddAccount.AddAccountResponse;
import com.privatepe.host.response.UdateAccountResponse;
import com.privatepe.host.utils.Constant;


public class AddAccountActivity extends AppCompatActivity implements ApiResponseInterface, View.OnTouchListener {
    ActivityAddAccountBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        binding.setClickListener(new EventHandler(this));
        apiManager = new ApiManager(this, this);
        apiManager.getAddAccountDetail();

        binding.etIfscCode.setFilters(new InputFilter[]{filter});
        InputFilter filter_account = new InputFilter.LengthFilter(16);
        binding.etAccountNumber.setFilters(new InputFilter[]{filter,filter_account});
        InputFilter filter_phone = new InputFilter.LengthFilter(10);
        binding.etPhoneNumber.setFilters(new InputFilter[]{filter,filter_phone});
        binding.etAccountNumber.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        binding.etAccountNumber.setLongClickable(false);
        binding.etAccountNumber.setTextIsSelectable(false);

        binding.etUserName.setOnTouchListener(this);
        binding.rlBankName.setOnTouchListener(this);
        binding.etIfscCode.setOnTouchListener(this);
        binding.etAccountNumber.setOnTouchListener(this);
        binding.etUserAddress.setOnTouchListener(this);
        binding.etEmailId.setOnTouchListener(this);
        binding.etPhoneNumber.setOnTouchListener(this);


    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPage() {
            finish();
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

        public void saveData() {
            if (isMandatoryFields()) {
                apiManager.upDateAccount(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(),"",
                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                        binding.etPhoneNumber.getText().toString(),"","1");
                Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
            }

        }

        public void saveDetails() {
            if (isMandatoryFields()) {
                apiManager.upDateAccount(binding.etUserName.getText().toString(), binding.tvBankCodeInput.getText().toString(),
                        binding.etIfscCode.getText().toString().trim(), binding.etAccountNumber.getText().toString(),"",
                        binding.etUserAddress.getText().toString(), binding.etEmailId.getText().toString(),
                        binding.etPhoneNumber.getText().toString(),"","1");
                Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (view.getId()) {
            case R.id.et_user_name:
                binding.etUserName.addTextChangedListener(new TextWatcher() {
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
            case R.id.tv_bank_code_input:
                binding.tvBankCodeInput.addTextChangedListener(new TextWatcher() {
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
                            binding.tvError1.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError2.setVisibility(View.VISIBLE);

                        }
                    }
                });

                break;
            case R.id.et_ifsc_code:
                binding.etIfscCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() > 4) {
                            binding.tvError2.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError2.setVisibility(View.VISIBLE);

                        }
                    }
                });

                break;
            case R.id.et_account_number:
                binding.etAccountNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() >= 12) {
                            binding.tvError3.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError3.setVisibility(View.VISIBLE);

                        }
                    }
                });

                break;
            case R.id.et_user_address:
                binding.etUserAddress.addTextChangedListener(new TextWatcher() {
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
                            binding.tvError4.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError4.setVisibility(View.VISIBLE);

                        }

                    }
                });
                break;
            case R.id.et_email_id:
                binding.etEmailId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String email = binding.etEmailId.getText().toString();
                        if (email.matches(emailPattern) && charSequence.length() > 0) {
                            binding.tvError5.setText("valid email");
                        } else {
                            binding.tvError5.setText("invalid email");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (txt.matches(emailPattern) && s.length() >= 10) {
                            binding.tvError5.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError5.setVisibility(View.VISIBLE);

                        }

                    }
                });
                break;
            case R.id.et_phone_number:
                binding.etPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (charSequence.length() >= 10) {
                            binding.tvError6.setText("valid Phone number");
                        } else {
                            binding.tvError6.setText("invalid Phone number");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String txt = s.toString();
                        if (!txt.isEmpty() && s.length() >= 10) {
                            binding.tvError6.setVisibility(View.INVISIBLE);
                        } else {
                            binding.tvError6.setVisibility(View.VISIBLE);
                        }

                    }
                });

                break;
            default:

                break;
        }

        return false;
    }


    private boolean isMandatoryFields() {
        if (binding.etUserName.getText().toString().isEmpty()) {
            return false;
        } else if (binding.tvBankCodeInput.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etIfscCode.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etAccountNumber.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etUserAddress.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etEmailId.getText().toString().isEmpty()) {
            return false;
        } else if (binding.etPhoneNumber.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }


    @Override
    public void isError(String errorCode) {
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_ADD_ACCOUNT_DETAIL) {
            AddAccountResponse rsp = (AddAccountResponse) response;

            for(int i = 0; i<rsp.getResult().size(); i++) {
                if(rsp.getResult().get(i).getType().equals("1")) {
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
        }
        if (ServiceCode == Constant.UPDATE_ACCOUNT) {
            UdateAccountResponse rsp = (UdateAccountResponse) response;
            Log.e("UpdateData", "UpdateData1 " + rsp.getResult());


        }


    }

   /*private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;

        public CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.hashCode() > 0) {
                isMandatoryFieldsFill();
            } else {
                isMandatoryFields();
            }

        }

    }*/


    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "click BACK again to go Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }


}